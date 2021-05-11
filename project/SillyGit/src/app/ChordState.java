package app;

import data.Data;
import data.Key;
import data.Value;
import message.PullAskMessage;
import message.PushMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChordState {

    private final int level;
    private final Set<Servent> servents;
    private Servent predecessor;
    private final Servent[] successors;
    private Map<Key, Value> chunk;

    public ChordState() {
        level = (int) Math.log(Config.CHORD_SIZE);
        successors = new Servent[level];
        servents = new TreeSet<>();
        chunk = new HashMap<>();
    }

    public void initialize(Servent successor, Map<Key, Value> chunk) {
        successors[0] = successor;

        if (chunk != null) {
            this.chunk.putAll(chunk);
        }
    }

    public boolean isCollision(int chordId) {
        if (chordId == Config.LOCAL_SERVENT.getChordId()) {
            return true;
        }

        for (Servent servent : servents) {
            if (servent.getChordId() == chordId) {
                return true;
            }
        }

        return false;
    }

    public boolean containsKey(Key key) {
        if (predecessor == null) {
            return true;
        }

        int local = Config.LOCAL_SERVENT.getChordId();
        int predecessor = this.predecessor.getChordId();
        int id = key.get();

        return id <= local && id > predecessor || (local <= predecessor && (id <= local || id > predecessor));
    }

    public Servent getServent(Key key) {
        if (containsKey(key)) {
            return Config.LOCAL_SERVENT;
        }

        int index = 0;

        if (key.get() < Config.LOCAL_SERVENT.getChordId()) {
            int skip = 1;

            while (successors[skip].getChordId() > successors[index].getChordId()) {
                index++;
                skip++;
            }
        }

        int previous = successors[index].getChordId();

        for (int i = index + 1; i < successors.length; i++) {
            int current = successors[i].getChordId();

            if (current >= key.get() || current < previous && previous < key.get()) {
                return successors[i - 1];
            }

            previous = current;
        }

        return successors[0];
    }

    private void updateSuccessors() {
        int index = 0;

        List<Servent> servents = new ArrayList<>(this.servents);

        Servent currServent = servents.get(index);
        Servent prevServent = Config.LOCAL_SERVENT;

        successors[index] = currServent;

        for (int i = 1; i < level; i++) {
            int currentValue = (Config.LOCAL_SERVENT.getChordId() + (int) Math.pow(2, i)) % Config.CHORD_SIZE;

            int currId = currServent.getChordId();
            int prevId = prevServent.getChordId();

            while (true) {
                Servent nextServent = servents.get((index + 1) % servents.size());
                int nextId = nextServent.getChordId();

                if ((currentValue > currId && currId > prevId || currentValue < prevId)
                        || (nextId < currId && currentValue <= nextId)) {
                    prevId = currId;
                    index = (index + 1) % servents.size();
                    currServent = servents.get(index);
                    currId = currServent.getChordId();
                } else {
                    successors[i] = currServent;
                    break;
                }
            }
        }
    }

    public void addServents(List<Servent> newNodes) {
        servents.addAll(newNodes);

        List<Servent> after = new ArrayList<>();
        List<Servent> before = new ArrayList<>();

        int local = Config.LOCAL_SERVENT.getChordId();

        for (Servent servent : servents) {
            if (servent.getChordId() < local) {
                before.add(servent);
            } else {
                after.add(servent);
            }
        }

        servents.clear();
        servents.addAll(after);
        servents.addAll(before);

        if (before.isEmpty()) {
            setPredecessor(after.get(after.size() - 1));
        } else {
            setPredecessor(before.get(before.size() - 1));
        }

        updateSuccessors();
    }

    public Value getValue(Key key) {
        if (containsKey(key)) {
            return chunk.getOrDefault(key, new Value(-1));
        }

        App.send(new PullAskMessage(getServent(key), key));

        return null;
    }

    public void putValue(Key key, Value value) {
        if (containsKey(key)) {
            chunk.put(key, value);
        } else {
            App.send(new PushMessage(getServent(key), new Data(key, value)));
        }
    }

    public Servent getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Servent predecessor) {
        this.predecessor = predecessor;
    }

    public Servent[] getSuccessors() {
        return successors;
    }

    public Servent getNextServent() {
        return successors[0];
    }

    public Map<Key, Value> getChunk() {
        return new ConcurrentHashMap<>(chunk);
    }

    public void setChunk(Map<Key, Value> chunk) {
        this.chunk = new ConcurrentHashMap<>(chunk);
    }
}
