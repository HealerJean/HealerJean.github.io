package com.hlj.arith.demo00144_机器人能否返回原点;

import org.junit.Test;

import java.util.*;

/**
 * 作者：HealerJean
 题目：
 在二维平面上，有一个机器人从原点 (0, 0) 开始。给出它的移动顺序，判断这个机器人在完成移动后是否在 (0, 0) 处结束。
 注意：机器人“面朝”的方向无关紧要。 “R” 将始终使机器人向右移动一次，“L” 将始终向左移动等。此外，假设每次移动机器人的移动幅度相同。
     示例 1:
         输入: "UD"
         输出: true
         解释：机器人向上移动一次，然后向下移动一次。所有动作都具有相同的幅度，因此它最终回到它开始的原点。因此，我们返回 true。
     示例 2:
         输入: "LL"
         输出: false
         解释：机器人向左移动两次。它最终位于原点的左侧，距原点有两次 “移动” 的距离。我们返回 false，因为它在移动结束时没有返回原点。
         解题思路：
 */
public class 机器人能否返回原点 {

    @Test
    public void test() {
        System.out.println(judgeCircle2("LDRRLRUULR"));

    }

    /**
     * 方案1 List解决
     */
    public boolean judgeCircle(String moves) {
        Map<Character, Character> map = new HashMap<>();
        map.put('U','D');
        map.put('D','U');
        map.put('R','L');
        map.put('L','R');

        List<Character> set = new ArrayList<>();
        for (int i = 0; i < moves.length(); i++) {
            Character key = moves.charAt(i);
            Character value = map.get(key);
            if (set.contains(key)){
                set.remove(key);
            }else {
                set.add(value);
            }
        }
        return set.isEmpty();
    }

    /**
     * 方案2：计数 （推荐）
     */
    public boolean judgeCircle2(String moves) {
        int U = 0 , D = 0 , L = 0, R = 0 ;
        for (int i = 0; i < moves.length(); i++) {
            Character character = moves.charAt(i);
            if (character == 'U'){
                U++;
            }
            if (character == 'D'){
                D++;
            }
            if (character == 'L'){
                L++;
            }
            if (character == 'R'){
                R++;
            }
        }
        //上和下，左和右相等
        return  U == D && L == R ;
    }
}
