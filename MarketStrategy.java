import java.util.List;
import java.util.stream.Collectors;

/*
 * Shows how to buy items from market
 */
public class MarketStrategy {

        private IOHelper iohelper;

        public MarketStrategy(IOHelper iohelper) {
                this.iohelper = iohelper;
        }

        void act(HeroParty heroParty, Market market) {
                List<String> itemNames = market.getItems().stream().map(i -> i.getName())
                                .collect(Collectors.toList());

                iohelper.println("marker has the following items and prices");
                market.getItems().stream()
                                .forEach(e -> iohelper.println(e.getName() + " : " + e.getPrice()));

                List<String> heroNames = heroParty.getHeroes().stream().map(Hero::getName).collect(Collectors.toList());
                iohelper.println("heroes are: " + String.join(",", heroNames));

                while (true) {
                        String heroName = iohelper.nextLine("for which hero do you want to sell or buy(q to quit):",
                                        "not a hero",
                                        s -> heroNames.contains(s) || s.equals("q"));
                        if (heroName.equals("q")) {
                                return;
                        }
                        Hero hero = heroParty.getHeroes().stream().filter(h -> h.getName().equals(heroName)).findAny()
                                        .get();
                        iohelper.println(hero.getName() + " has " + hero.getGold() + " gold");
                        String buy = iohelper.nextLine("do you want to buy any item?(y/n)", "not y or n",
                                        s -> s.equalsIgnoreCase("y") || s.equalsIgnoreCase("n"));

                        if (buy.equals("y")) {
                                String itemName = iohelper.nextLine("which item do you want to buy?",
                                                "not a valid item",
                                                s -> itemNames.contains(s));
                                Item item = market.getItems().stream()
                                                .filter(e -> e.getName().equals(itemName)).findAny().get();
                                if (hero.getInventory().contains(item)) {
                                        iohelper.printErr("hero already has the item");
                                        continue;
                                }
                                if (hero.getGold() < item.getPrice()) {
                                        iohelper.printErr("hero does not have enough gold");
                                        continue;
                                }
                                hero.addItem(item);
                                hero.decreaseGold(item.getPrice());
                        }

                        String sell = iohelper.nextLine("do you want to sell any item?(y/n)", "not y or n",
                                        s -> s.toLowerCase().equals("y") || s.toLowerCase().equals("n"));

                        if (sell.equals("y")) {
                                List<String> heroItemNames = hero.getInventory().stream().map(i -> i.getName())
                                                .collect(Collectors.toList());
                                iohelper.println("items with the hero are: " + String.join(",", heroItemNames));
                                String sellItemName = iohelper.nextLine("which item do you want to sell?",
                                                "not a valid item",
                                                s -> heroItemNames.contains(s));
                                Item sellItem = hero.getInventory().stream()
                                                .filter(i -> i.getName().equals(sellItemName)).findAny()
                                                .get();
                                hero.removeItem(sellItem);
                                hero.increaseGold(sellItem.getPrice() / 2);
                        }
                }

        }

}
