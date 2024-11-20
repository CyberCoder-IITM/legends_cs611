import java.util.Set;

/*
 * Market containing all items
 */

public class Market {
    private Set<Item> items;
    private static final double SELL_PRICE_RATIO = 0.5;

    public Market(Set<Item> items) {
        this.items = items;
    }

    public Set<Item> getItems() {
        return items;
    }

    // Add method to handle market transactions
    public void handleTransaction(Hero hero, IOHelper ioHelper) {
        // Check if hero is in nexus
        if (!hero.getCurrentPosition().isHeroNexus()) {
            ioHelper.printErr("Must be in Nexus to access market!");
            return;
        }

        while (true) {
            String choice = ioHelper.nextLine(
                    "Market Options:\n1. Buy\n2. Sell\n3. Exit\nChoice: ",
                    "Invalid choice",
                    s -> s.matches("[123]")
            );

            switch (choice) {
                case "1":
                    handleBuying(hero, ioHelper);
                    break;
                case "2":
                    handleSelling(hero, ioHelper);
                    break;
                case "3":
                    return;
            }
        }
    }

    private void handleBuying(Hero hero, IOHelper ioHelper) {
        displayItems(ioHelper);
        ioHelper.println("Your gold: " + hero.getGold());

        String itemName = ioHelper.nextLine(
                "Enter item name to buy (or 'q' to quit): ",
                "Invalid item",
                s -> s.equals("q") || items.stream()
                        .anyMatch(i -> i.getName().equals(s))
        );

        if (itemName.equals("q")) return;

        Item item = items.stream()
                .filter(i -> i.getName().equals(itemName))
                .findFirst().get();

        if (hero.getGold() < item.getPrice()) {
            ioHelper.printErr("Not enough gold!");
            return;
        }

        hero.decreaseGold(item.getPrice());
        hero.addItem(item);
        ioHelper.println("Purchased " + item.getName());
    }

    private void handleSelling(Hero hero, IOHelper ioHelper) {
        if (hero.getInventory().isEmpty()) {
            ioHelper.println("No items to sell!");
            return;
        }

        displayInventory(hero, ioHelper);

        String itemName = ioHelper.nextLine(
                "Enter item name to sell (or 'q' to quit): ",
                "Invalid item",
                s -> s.equals("q") || hero.getInventory().stream()
                        .anyMatch(i -> i.getName().equals(s))
        );

        if (itemName.equals("q")) return;

        Item item = hero.getInventory().stream()
                .filter(i -> i.getName().equals(itemName))
                .findFirst().get();

        double sellPrice = item.getPrice() * SELL_PRICE_RATIO;
        hero.increaseGold(sellPrice);
        hero.removeItem(item);
        ioHelper.println("Sold " + item.getName() + " for " + sellPrice + " gold");
    }

    private void displayItems(IOHelper ioHelper) {
        ioHelper.println("\nAvailable Items:");
        ioHelper.println("----------------");
        for (Item item : items) {
            ioHelper.println(String.format("%-20s | Price: %-6.0f | Type: %s",
                    item.getName(), item.getPrice(), item.getType()));
        }
    }

    private void displayInventory(Hero hero, IOHelper ioHelper) {
        ioHelper.println("\nYour Inventory:");
        ioHelper.println("--------------");
        for (Item item : hero.getInventory()) {
            double sellPrice = item.getPrice() * SELL_PRICE_RATIO;
            ioHelper.println(String.format("%-20s | Sell Price: %-6.0f | Type: %s",
                    item.getName(), sellPrice, item.getType()));
        }
    }
}
