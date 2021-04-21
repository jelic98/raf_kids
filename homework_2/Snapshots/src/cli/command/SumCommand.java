package cli.command;

import app.AppConfig;
import servent.message.MessageUtil;
import servent.message.SumHelpMessage;

import java.math.BigInteger;

public class SumCommand implements CLICommand {

    public static BigInteger friendResult = null;

    @Override
    public String commandName() {
        return "sum";
    }

    @Override
    public void execute(String args) {
        try {
            BigInteger number = new BigInteger(args);

            BigInteger mid = number.divide(new BigInteger("2"));

            int friendId = -1;
            if (AppConfig.myServentInfo.getId() % 2 == 0) {
                friendId = AppConfig.myServentInfo.getId() + 1;
            } else {
                friendId = AppConfig.myServentInfo.getId() - 1;
            }

            MessageUtil.sendMessage(
                    new SumHelpMessage(
                            AppConfig.myServentInfo, AppConfig.getInfoById(friendId), mid));

            BigInteger rightSum = BigInteger.ZERO;
            BigInteger counter = mid;

            for (; counter.compareTo(number) == -1; counter = counter.add(BigInteger.ONE)) {
                rightSum = rightSum.add(counter);
            }

            while (friendResult == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Sum result: " + rightSum.add(friendResult).toString());

            friendResult = null;

        } catch (NumberFormatException e) {
            System.err.println("Bad argument for sum: " + args + ". Should be an integer.");
        }

    }

}
