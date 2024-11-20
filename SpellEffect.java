public enum SpellEffect {
    FREEZE {
        @Override
        public void apply(Monster target) {
            // Implement freeze effect
        }
    },
    BURN {
        @Override
        public void apply(Monster target) {
            // Implement burn effect
        }
    },
    STUN {
        @Override
        public void apply(Monster target) {
            // Implement stun effect
        }
    };

    public abstract void apply(Monster target);
}
