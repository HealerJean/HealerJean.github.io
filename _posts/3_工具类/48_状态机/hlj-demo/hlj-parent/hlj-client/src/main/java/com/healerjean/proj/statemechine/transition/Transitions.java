package com.healerjean.proj.statemechine.transition;

import com.healerjean.proj.statemechine.IStateMachine;
import org.apache.commons.compress.utils.Lists;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * Transitions
 *
 * @author zhangyujin
 * @date 2023/6/28$  09:43$
 */
public class Transitions<S extends Enum<S>, E extends Enum<E>> {

    /**
     * 所有转换
     */
    private List<Transition> transitions = Lists.newArrayList();
    /**
     * stateMachine
     */
    private IStateMachine stateMachine;

    public Transitions() {
    }

    public S accept(S source, E event) {
        Optional<Transition> transition = this.transitions.stream().filter(t -> t.accept(source, event)).findFirst();
        return transition.map(Transition::getTarget).orElse(null);
    }

    public Transitions setStateMachine(IStateMachine stateMachine) {
        this.stateMachine = stateMachine;
        return this;
    }

    public Transition withTransition() {
        return this.add(new Transition(this.stateMachine));
    }

    private Transition add(Transition transition) {
        this.transitions.add(transition);
        return transition;
    }

    public List<Transition> getTransitions() {
        return this.transitions;
    }

    public class Transition {
        private EnumSet<S> source;
        private S target;
        private E event;
        private IStateMachine stateMachine;

        public Transition(IStateMachine stateMachine) {
            this.stateMachine = stateMachine;
        }

        public Transition source(S state) {
            this.source = EnumSet.of(state);
            return this;
        }

        public Transition source(EnumSet<S> states) {
            this.source = states;
            return this;
        }

        public Transition target(S state) {
            this.target = state;
            return this;
        }

        public Transition event(E event) {
            this.event = event;
            return this;
        }

        public Transitions and() {
            return this.stateMachine.getTransitions();
        }

        /**
         * 状态机核心校验逻辑
         *
         * @param source 当前状态
         * @param event  当前具体状态机事件
         * @return 是否接收该状态机事件
         */
        boolean accept(S source, E event) {
            return this.source.contains(source) && this.event.equals(event);
        }

        public EnumSet<S> getSource() {
            return this.source;
        }

        public S getTarget() {
            return this.target;
        }

        public E getEvent() {
            return this.event;
        }

        public IStateMachine getStateMachine() {
            return this.stateMachine;
        }
    }
}
