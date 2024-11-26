import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * Represents a hero
// */
public class Hero {
    private String name;
    private HeroType type;
    private int level;
    private int exp;
    private HeroStats stats;
    private int gold;
    private Set<Item> inventory;
    private IOHelper ioHelper;
    private Board<CellType> board;
    //    private Map<EquipmentSlot, Item> equippedItems;
protected Map<EquipmentSlot, Item> equippedItems;
    private Position currentPosition;
    private Position nexusPosition;
    private Lane assignedLane;
    private boolean inCombat;
    private static final double RESTORE_RATE = 0.10; // 10% restoration

    public Hero(String name, HeroType type, int level, int exp, HeroStats stats,
                int gold, Set<Item> inventory, Map<EquipmentSlot, Item> equippedItems,
                Position nexusPosition, Lane lane) {
        this.inCombat = false;
        this.name = name;
        this.board = board;
        this.type = type;
        this.level = level;
        this.exp = exp;
        this.stats = stats;
        this.gold = gold;
        this.inventory = inventory;
        this.equippedItems = equippedItems;
        this.currentPosition = nexusPosition;
        this.nexusPosition = nexusPosition;
        this.assignedLane = lane;
    }

    // Basic getters
    public String getName() { return name; }
    public HeroType getType() { return type; }
    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public HeroStats getStats() { return stats; }
    public int getGold() { return gold; }
    public Set<Item> getInventory() { return inventory; }
    public Collection<Item> getEquippedItems() { return equippedItems.values(); }
    public Position getCurrentPosition() { return currentPosition; }
    public Position getNexusPosition() { return nexusPosition; }
    public Lane getAssignedLane() { return assignedLane; }

    // Movement methods
    public boolean move(Direction direction, Board<CellType> board) {

        if (currentPosition == null) {
            return false;
        }

        Position newPosition = currentPosition.move(direction);

        if (!isValidMove(newPosition, board)) {
            return false;
        }

        // Remove current terrain effects
//        board.removeTerrainEffect(currentPosition, this);
        // Remove terrain effects from current position
        if (board.hasTerrainEffect(currentPosition)) {
            board.removeTerrainEffect(currentPosition, this);
        }

        // Update position
        board.removeHero(currentPosition);
        currentPosition = newPosition;
        board.placeHero(currentPosition, this);


        // Apply new terrain effects
//        board.applyTerrainEffect(currentPosition, this);
        // Apply new terrain effects
        if (board.hasTerrainEffect(currentPosition)) {
            board.applyTerrainEffect(currentPosition, this);
        }

        return true;
    }

    public boolean isInCombat() {
        // Check if there are any monsters in attack range
        if (assignedLane != null) {
            List<Monster> nearbyMonsters = assignedLane.getMonstersInRange(currentPosition);
            return !nearbyMonsters.isEmpty();
        }
        return false;
    }

    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }


    public boolean doItemAction(Item item) {
        if (!canUseItem(item)) {
            return false;
        }

        switch (item.getType()) {
            case WEAPON:
                return equip((Weapon) item);
            case ARMOR:
                return equip((Armor) item);
            case POTION:
                return ((Potion) item).action(this);
            default:
                return false;
        }
    }

    public boolean castSpell(Item spell, Monster target) {
        if (!(spell.getType() == ItemType.SPELL)) {
            return false;
        }

        // Check if hero has enough mana
        if (stats.getMp() < ((Spell)spell).getManaCost()) {
            return false;
        }

        // Check if target is in range
        if (!canAttack(target.getCurrentPosition())) {
            return false;
        }

        // Apply spell damage and effect
        double damage = calculateSpellDamage((Spell)spell);
        target.takeDamage(damage);

        // Reduce hero's mana
        stats.decreaseMP(((Spell)spell).getManaCost());

        // Apply spell effect if target is still alive
        if (target.getHp() > 0) {
            ((Spell)spell).applyEffect(target);
        }

        return true;
    }

    private boolean canUseItem(Item item) {
        // Check level requirement
        if (level < item.getLevelRequirement()) {
            return false;
        }

        // Check if item is in inventory
        if (!inventory.contains(item)) {
            return false;
        }

        return true;
    }

    private double calculateSpellDamage(Spell spell) {
        // Base spell damage
        double damage = spell.damage();

        // Add dexterity bonus
        damage *= (1 + stats.getDex() / 10000);

        return damage;
    }


    // Optional: Add method to check for nearby monsters
    private boolean hasNearbyMonsters() {
        if (currentPosition == null || assignedLane == null) {
            return false;
        }

        // Check adjacent positions for monsters
        for (Position pos : currentPosition.getAdjacentPositions()) {
            if (assignedLane.hasMonsterAt(pos)) {
                return true;
            }
        }
        return false;
    }



    public boolean teleport(Position targetPosition, Board<CellType> board) {
        if (!canTeleportTo(targetPosition, board)) {
            return false;
        }

        board.removeTerrainEffect(currentPosition, this);
        currentPosition = targetPosition;
        board.applyTerrainEffect(currentPosition, this);
        return true;
    }

    public void recall(Board<CellType> board) {
        board.removeTerrainEffect(currentPosition, this);
        currentPosition = nexusPosition;
        board.applyTerrainEffect(currentPosition, this);
    }

    // Combat methods
    public boolean canAttack(Position targetPosition) {
        int dx = Math.abs(targetPosition.getX() - currentPosition.getX());
        int dy = Math.abs(targetPosition.getY() - currentPosition.getY());
        return (dx <= 1 && dy <= 1) && (dx + dy <= 1);
    }

    public double damageValue() {
        double weaponDamage = equippedItems.values().stream()
                .filter(item -> item.getType() == ItemType.WEAPON)
                .mapToDouble(Item::damage)
                .sum();
        return (weaponDamage + stats.getStr()) * 0.05;
    }

    public void takeDamage(double damage) {
        stats.decreaseHP(damage);
        if (stats.getHp() <= 0) {
            respawn();
        }
    }
    // Add these methods to your Hero class

    public boolean hasWeaponEquipped(EquipmentSlot slot) {
        Item equippedItem = equippedItems.get(slot);
        return equippedItem != null && equippedItem.getType() == ItemType.WEAPON;
    }

    public boolean canEquipWeapon(Weapon weapon) {
        // Check if hero meets level requirement
        if (getLevel() < weapon.getLevel()) {
            return false;
        }

        // For two-handed weapons
        if (weapon.getHands() == 2) {
            return !hasWeaponEquipped(EquipmentSlot.LEFT_HAND) &&
                    !hasWeaponEquipped(EquipmentSlot.RIGHT_HAND);
        }

        // For one-handed weapons
        return !hasWeaponEquipped(EquipmentSlot.LEFT_HAND) ||
                !hasWeaponEquipped(EquipmentSlot.RIGHT_HAND);
    }

    public void equipWeapon(Weapon weapon) {
        if (weapon.getHands() == 2) {
            equippedItems.put(EquipmentSlot.LEFT_HAND, weapon);
            equippedItems.put(EquipmentSlot.RIGHT_HAND, weapon);
        } else {
            if (!hasWeaponEquipped(EquipmentSlot.LEFT_HAND)) {
                equippedItems.put(EquipmentSlot.LEFT_HAND, weapon);
            } else {
                equippedItems.put(EquipmentSlot.RIGHT_HAND, weapon);
            }
        }
    }


    // Equipment methods
    public boolean equip(Weapon weapon) {
        if (weapon.getHands() == 2) {
            equippedItems.put(EquipmentSlot.LEFT_HAND, weapon);
            equippedItems.put(EquipmentSlot.RIGHT_HAND, weapon);
        } else {
            if (!equippedItems.containsKey(EquipmentSlot.LEFT_HAND)) {
                equippedItems.put(EquipmentSlot.LEFT_HAND, weapon);
            } else {
                equippedItems.put(EquipmentSlot.RIGHT_HAND, weapon);
            }
        }
        return true;
    }

    public boolean equip(Armor armor) {
        equippedItems.put(EquipmentSlot.BODY, armor);
        return true;
    }

    // Item management
    public boolean applyPotion(Potion potion) {
        potion.action(this);
        inventory.remove(potion);
        return true;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
        equippedItems.values().remove(item);
    }

    // Gold management
    public void increaseGold(Double amount) {
        this.gold += amount;
    }

    public void decreaseGold(Double amount) {
        this.gold -= amount;
    }

    // Round end and respawn
    public void respawn() {
        stats.fullRestore();
        currentPosition = nexusPosition;
    }

    public void restoreAfterRound() {
        if (stats.getHp() > 0) {
            stats.restorePercentHP(RESTORE_RATE);
            stats.restorePercentMP(RESTORE_RATE);
        }
    }

    private boolean isValidMove(Position newPos, Board<CellType> board) {

        // Check basic validity
        if (!board.isValid(newPos)) {
            return false;
        }

        // Check if position is in assigned lane
        if (!assignedLane.contains(newPos)) {
            return false;
        }

        // Check for inaccessible spaces
        if (board.isInaccessible(newPos)) {
            return false;
        }

        // Check for other heroes
        if (board.hasHero(newPos)) {
            return false;
        }

        // Check for monsters ahead
        if (hasMonsterAhead(newPos, board)) {
            return false;
        }

        return true;

    }

//    private boolean hasMonsterAhead(Position pos, Board<CellType> board) {
//        return assignedLane.hasMonsterAhead(pos);
//    }

    private boolean hasMonsterAhead(Position newPos, Board<CellType> board) {
        // Check if trying to move behind a monster
        if (newPos.getX() < currentPosition.getX()) {  // Moving north
            for (int row = newPos.getX(); row <= currentPosition.getX(); row++) {
                Position checkPos = new Position(row, newPos.getY());
                if (board.hasMonster(checkPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canTeleportTo(Position target, Board<CellType> board) {
        return !currentPosition.isInSameLane(target) &&
                !board.hasHero(target) &&
                !assignedLane.hasMonsterAhead(target);
    }

    public void setCurrentPosition(Position position) {
        this.currentPosition = position;
    }

}