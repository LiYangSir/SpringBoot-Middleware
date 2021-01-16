package cn.quguai.lockzk.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedPacketUtil {
    public static List<Integer> divideRedPacket(Integer totalAmount, Integer totalPeopleNum) {
        List<Integer> amountList = new ArrayList<>();
        Random random = new Random();
        Integer resAmount = totalAmount;
        Integer resPeopleNum = totalPeopleNum;
        if (totalAmount > 0 && totalPeopleNum > 0) {
            for (int i = 0; i < totalPeopleNum - 1; i++) {
                int amount = random.nextInt(resAmount / resPeopleNum);
                amountList.add(amount);
                resAmount = resAmount - amount;
                resPeopleNum--;
            }
            amountList.add(resAmount);
        }
        return amountList;
    }
}
