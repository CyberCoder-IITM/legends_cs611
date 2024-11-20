public class Spell extends Item {
    private int manaCost;
    private double baseDamage;
    private SpellEffect effect;

    public Spell(String name, double price, int level, int manaCost, double baseDamage, SpellEffect effect) {
        super(name, ItemType.SPELL, price, level);
        this.manaCost = manaCost;
        this.baseDamage = baseDamage;
        this.effect = effect;
    }

    public int getManaCost() {
        return manaCost;
    }

    @Override
    protected boolean action(Hero hero) {
        return false; // Spells are handled differently through castSpell
    }

    @Override
    protected double damage() {
        return baseDamage;
    }

    public void applyEffect(Monster target) {
        if (effect != null) {
            effect.apply(target);
        }
    }
}

