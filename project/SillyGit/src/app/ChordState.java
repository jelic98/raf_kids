package app;

import message.*;

import java.util.*;

// TODO Cleanup
public class ChordState {

    public static int CHORD_SIZE;

    private int chordLevel;
    private Servent[] successors;
    private Servent predecessorInfo;
    private List<Servent> allNodeInfo;
    private Map<Integer, Integer> valueMap;

    public ChordState() {
        chordLevel = (int) Math.log(CHORD_SIZE);
        successors = new Servent[chordLevel];

        for (int i = 0; i < chordLevel; i++) {
            successors[i] = null;
        }

        predecessorInfo = null;
        valueMap = new HashMap<>();
        allNodeInfo = new ArrayList<>();
    }

    public static int chordHash(int value) {
        return 61 * value % CHORD_SIZE;
    }

    public void init(WelcomeAskMessage welcomeMsg) {
        successors[0] = welcomeMsg.getSender();
        valueMap = welcomeMsg.getValues();
    }

    public boolean isCollision(int chordId) {
        if (chordId == Config.LOCAL_SERVENT.getChordId()) {
            return true;
        }

        for (Servent servent : allNodeInfo) {
            if (servent.getChordId() == chordId) {
                return true;
            }
        }

        return false;
    }

    public boolean isKeyMine(int key) {
        if (predecessorInfo == null) {
            return true;
        }

        int predecessorChordId = predecessorInfo.getChordId();
        int myChordId = Config.LOCAL_SERVENT.getChordId();

        if (predecessorChordId < myChordId) {
            return key <= myChordId && key > predecessorChordId;
        }

        return key <= myChordId || key > predecessorChordId;
    }

    public Servent getServent(int key) {
        if (isKeyMine(key)) {
            return Config.LOCAL_SERVENT;
        }

        int startInd = 0;

        if (key < Config.LOCAL_SERVENT.getChordId()) {
            int skip = 1;
            while (successors[skip].getChordId() > successors[startInd].getChordId()) {
                startInd++;
                skip++;
            }
        }

        int previousId = successors[startInd].getChordId();

        for (int i = startInd + 1; i < successors.length; i++) {
            if (successors[i] == null) {
                App.error("Couldn't find successor for " + key);
                break;
            }

            int successorId = successors[i].getChordId();

            if (successorId >= key) {
                return successors[i - 1];
            }

            if (key > previousId && successorId < previousId) { //overflow
                return successors[i - 1];
            }

            previousId = successorId;
        }

        return successors[0];
    }

    private void updateSuccessorTable() {
        int currentNodeIndex = 0;
        Servent currentNode = allNodeInfo.get(currentNodeIndex);
        successors[0] = currentNode;

        int currentIncrement = 2;

        Servent previousNode = Config.LOCAL_SERVENT;

        for (int i = 1; i < chordLevel; i++, currentIncrement *= 2) {
            int currentValue = (Config.LOCAL_SERVENT.getChordId() + currentIncrement) % CHORD_SIZE;

            int currentId = currentNode.getChordId();
            int previousId = previousNode.getChordId();

            while (true) {
                if (currentValue > currentId) {
                    if (currentId > previousId || currentValue < previousId) {
                        previousId = currentId;
                        currentNodeIndex = (currentNodeIndex + 1) % allNodeInfo.size();
                        currentNode = allNodeInfo.get(currentNodeIndex);
                        currentId = currentNode.getChordId();
                    } else {
                        successors[i] = currentNode;
                        break;
                    }
                } else {
                    Servent nextNode = allNodeInfo.get((currentNodeIndex + 1) % allNodeInfo.size());
                    int nextNodeId = nextNode.getChordId();
                    if (nextNodeId < currentId && currentValue <= nextNodeId) {
                        previousId = currentId;
                        currentNodeIndex = (currentNodeIndex + 1) % allNodeInfo.size();
                        currentNode = allNodeInfo.get(currentNodeIndex);
                        currentId = currentNode.getChordId();
                    } else {
                        successors[i] = currentNode;
                        break;
                    }
                }
            }
        }
    }

    public void addServents(List<Servent> newNodes) {
        allNodeInfo.addAll(newNodes);
        allNodeInfo.sort(new Comparator<Servent>() {
            @Override
            public int compare(Servent s1, Servent s2) {
                return s1.getChordId() - s2.getChordId();
            }
        });

        List<Servent> newList = new ArrayList<>();
        List<Servent> newList2 = new ArrayList<>();

        int myId = Config.LOCAL_SERVENT.getChordId();

        for (Servent serventInfo : allNodeInfo) {
            if (serventInfo.getChordId() < myId) {
                newList2.add(serventInfo);
            } else {
                newList.add(serventInfo);
            }
        }

        allNodeInfo.clear();
        allNodeInfo.addAll(newList);
        allNodeInfo.addAll(newList2);

        if (newList2.size() > 0) {
            predecessorInfo = newList2.get(newList2.size() - 1);
        } else {
            predecessorInfo = newList.get(newList.size() - 1);
        }

        updateSuccessorTable();
    }

    public void putValue(int key, int value) {
        if (isKeyMine(key)) {
            valueMap.put(key, value);
        } else {
            App.send(new PushMessage(getServent(key), key, value));
        }
    }

    public int getValue(int key) {
        if (isKeyMine(key)) {
            return valueMap.getOrDefault(key, -1);
        }

        App.send(new PullAskMessage(getServent(key), key));

        return -2;
    }

    public int getChordLevel() {
        return chordLevel;
    }

    public List<Servent> getSuccessors() {
        return Arrays.asList(successors);
    }

    public Servent getNextServent() {
        return successors[0];
    }

    public Servent getPredecessor() {
        return predecessorInfo;
    }

    public void setPredecessor(Servent newNodeInfo) {
        this.predecessorInfo = newNodeInfo;
    }

    public Map<Integer, Integer> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<Integer, Integer> valueMap) {
        this.valueMap = valueMap;
    }
}
