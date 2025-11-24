package bricker.brick_strategies;

public class StrategyFactory {

    public StrategyFactory() {
    }

    public CollisionStrategy generate(CollisionStrategy[] strategies) {
        return strategies[0];
    }
}
