
public class TerrainEffect {
    private static final double BONUS_PERCENTAGE = 0.1; // 10% bonus

    private CellType cellType;
    private double bonusValue;

    public TerrainEffect(CellType cellType) {
        this.cellType = cellType;
        this.bonusValue = BONUS_PERCENTAGE;
    }

    public void applyEffect(Hero hero) {
        switch(cellType) {
            case BUSH:
                hero.getStats().increaseDex(hero.getStats().getDex() * bonusValue);
                break;
            case CAVE:
                hero.getStats().increaseAgi(hero.getStats().getAgi() * bonusValue);
                break;
            case KOULOU:
                hero.getStats().increaseStr(hero.getStats().getStr() * bonusValue);
                break;
            default:
                break;
        }
    }

    public void removeEffect(Hero hero) {
        switch(cellType) {
            case BUSH:
                hero.getStats().increaseDex(-(hero.getStats().getDex() * bonusValue));
                break;
            case CAVE:
                hero.getStats().increaseAgi(-(hero.getStats().getAgi() * bonusValue));
                break;
            case KOULOU:
                hero.getStats().increaseStr(-(hero.getStats().getStr() * bonusValue));
                break;
            default:
                break;
        }
    }
}
