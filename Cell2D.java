
/*
 * A cell in a 2D Board
 */

public class Cell2D<T> {
    private T value;
    private TerrainEffect terrainEffect;

    public Cell2D(T value) {
        this.value = value;
        if (value instanceof CellType) {
            CellType cellType = (CellType) value;
            if (cellType == CellType.BUSH ||
                    cellType == CellType.CAVE ||
                    cellType == CellType.KOULOU) {
                this.terrainEffect = new TerrainEffect(cellType);
            }
        }
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        // Update terrain effect if cell type changes
        if (value instanceof CellType) {
            CellType cellType = (CellType) value;
            if (cellType == CellType.BUSH ||
                    cellType == CellType.CAVE ||
                    cellType == CellType.KOULOU) {
                this.terrainEffect = new TerrainEffect(cellType);
            } else {
                this.terrainEffect = null;
            }
        }
    }

    public TerrainEffect getTerrainEffect() {
        return terrainEffect;
    }

    public boolean hasTerrainEffect() {
        return terrainEffect != null;
    }

    public void applyTerrainEffect(Hero hero) {
        if (terrainEffect != null) {
            terrainEffect.applyEffect(hero);
        }
    }

    public void removeTerrainEffect(Hero hero) {
        if (terrainEffect != null) {
            terrainEffect.removeEffect(hero);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cell2D)) return false;
        Cell2D<?> other = (Cell2D<?>) obj;
        return value != null ? value.equals(other.value) : other.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "";
    }
}
