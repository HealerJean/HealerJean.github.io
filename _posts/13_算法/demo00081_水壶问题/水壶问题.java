package com.hlj.arith.demo00081_水壶问题;

import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：水壶问题
 有两个容量分别为 x升 和 y升 的水壶以及无限多的水。请判断能否通过使用这两个水壶，从而可以得到恰好 z升 的水？
 如果可以，最后请用以上水壶中的一或两个来盛放取得的 z升 水。
     示例 1: (From the famous "Die Hard" example)
         输入: x = 3, y = 5, z = 4
         输出: True
     示例 2:
         输入: x = 2, y = 6, z = 5
         输出: False

解题思路：
*/
public class 水壶问题 {


    @Test
    public void test(){
        int x = 3;
        int y = 5;
        int z = 4;

//        int x = 2;
//        int y = 6;
//        int z = 5;

//        int x = 1;
//        int y = 2;
//        int z = 3;
        boolean res = canMeasureWater(x, y, z);
        System.out.println(res);
    }


    public boolean canMeasureWater(int x, int y, int z) {
        // 特判
        if (z == 0) {
            return true;
        }
        if (x + y < z) {
            return false;
        }
        Queue<State> queue = new LinkedList<>();
        Set<State> existSet = new HashSet<>();


        State initState = new State(0, 0);
        //首次放入队列，表示肯定存在过
        queue.add(initState);
        existSet.add(initState);

        while (!queue.isEmpty()) {
            State state = queue.remove();
            int curX = state.getX();
            int curY = state.getY();
            //一定要记得，curX + curY == z
            if (curX == z || curY == z || curX + curY == z) {
                return true;
            }

            // 从当前状态获得所有可能的下一步的状态
            List<State> nextStates = getNextStates(curX, curY, x, y);

            for (State nextState : nextStates) {
                //如果访问过就不往里面放了，这样讲队列中的东西全部取出后，就表示结束这个while循环了
                if (!existSet.contains(nextState)) {
                    // 添加到队列以后，就弄成已访问
                    queue.add(nextState);
                    existSet.add(nextState);
                }
            }
        }
        return false;
    }

    /**
     * 都空了还玩个鸡鸡
     * @param curX
     * @param curY
     * @param x
     * @param y
     * @return
     */
    private List<State> getNextStates(int curX, int curY, int x, int y) {
        // 最多 8 个对象
        List<State> nextStates = new ArrayList<>(8);

        // 按理说应该先判断状态是否存在，再生成「状态」对象，这里为了阅读方便，一次生成 8 个对象
        // 以下两个状态，对应操作 1
        // 外部加水，使得 A 满
        State nextState1 = new State(x, curY);
        // 没有满的时候，才需要加水
        if (curX < x) {
            nextStates.add(nextState1);
        }

        // 外部加水，使得 B 满
        State nextState2 = new State(curX, y);
        // 没有满的时候，才需要加水
        if (curY < y) {
            nextStates.add(nextState2);
        }


        // 以下两个状态，对应操作 2
        // 把 A 清空
        State nextState3 = new State(0, curY);
        // 把 B 清空
        State nextState4 = new State(curX, 0);
        // 有水的时候，才需要倒掉
        if (curX > 0) {
            nextStates.add(nextState3);
        }
        if (curY > 0) {
            nextStates.add(nextState4);
        }



        // 以下四个状态，对应操作 3
        // 从 A 到 B，使得 B 满，A 还有剩
        State nextState5 = new State(curX - (y - curY), y);
        // 有剩余才倒
        if (curX - (y - curY) > 0) {
            nextStates.add(nextState5);
        }
        // 从 A 到 B，此时 A 的水太少，A 倒尽，B 没有满或者满了
        State nextState6 = new State(0, curX + curY);
        // 倒过去倒不满才倒
        if (curX + curY < y) {
            nextStates.add(nextState6);
        }



        // 从 B 到 A，使得 A 满，B 还有剩余
        State nextState7 = new State(x, curY - (x - curX));
        // 有剩余才倒
        if (curY - (x - curX) > 0) {
            nextStates.add(nextState7);
        }
        // 从 B 到 A，此时 B 的水太少，B 倒尽，A 没有满或者满了
        State nextState8 = new State(curX + curY, 0);
        if (curX + curY < x) {
            nextStates.add(nextState8);
        }
        return nextStates;
    }

    private class State {
        private int x;
        private int y;

        public State(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            State object = (State) o;
            return x == object.x && y == object.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }



}
