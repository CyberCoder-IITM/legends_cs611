# CS611-Assignment <4>
## < Legends: Monsters and Heroes >
---------------------------------------------------------------------------
- Name: Sundeep Routhu
- Email: srouthu@bu.edu
- Student ID: U77551834

## Files
---------------------------------------------------------------------------

**App.java**                    - Main class which runs the program

**GameRunner.java**             - Class which selects the game and plays it \
\
**Board.java**                  - 2D board of size n x m containing generic cells. For the games implemented, the cells are Heroes, Market and Walls.\
**Cell2D.java**                 - A generic cell in a 2D Board\
**Position.java**               - Represents a position in the 2D board\
**Position.java**               - Represents a direction\
\
**Item.java**                   - Represents an item being sold in the market\
**Items.java**                  - All items in the market\
**ItemType.java**               - Type of Item\
**Weapon.java**                 - Item which is a weapon\
**Armor.java**                  - Item which is an armor\
**Potion.java**                 - Item which is a poiton\
**PotionType.java**             - Type of potion\
\
**Hero.java**                   - Represents a singular hero\
**Heros.java**                  - All heroes\
**HeroParty.java**              - A party of heroes\
**HeroStats.java**              - Stats of the hero, like hp, mp etc.\
**HeroType.java**               - The type of hero\
**EquipmentSlot.java**          - Slots in which a hero can hold an item\
\
**Monster.java**                - Represents a singular monster \
**Monsters.java**               - All monsters \
**MonsterParty.java**           - A party of monsters\
**MonsterType.java**            - The type of monster\
**MonsterFactory.java**         - Generates monsters when heroes goes on a tile\
**RandomMonsterFactory.java**   - Generates random monsters when heroes goes on a tile\
\
**IOHelper.java**               - A utility class to print in console\
\
**Move.java**                   - Move made by the party of heroes\
**Legends.java**                - The core central loop controlling the game\
**WorldGenerator.java**         - Generates the world, heroes and market\
**FightStrategy.java**          - Strategy pattern to determine the fight\
**AllFightStrategy.java**       - Heroes can attack any monster in the monster party\

## Notes
---------------------------------------------------------------------------

### Features / Design Document
- A world is generated with random heroes in the party and a market
- Each turn the player gives input to decide the heroes move direction
    - "h" will give you the list of all inputs and what they will do
    - "w/a/s/d" moves the heroes
    - "p" prints the whole world
    - "i" shows the heroes information
    - "v" lets heroes modify their inventory
        - Heroes can equip items or use potions
    - "q" quits the game
- Market provides various items which the heroes can buy and equip
- Heroes can sell tems if they are on the market
- Monsters are generated randomly when the hero party goes on a tile
- Heroes can select the monster they want to fight with
- Fight ends when heroes die or monsters die, When all heroes dies then the game ends
- Have a fixed set of heroes, items, monsters
- Console output is colored(only in terminals which support ANSI escape codes)
    - Errors are in red
    - Prompts are in yellow
    - Output is in green
- Console rings a bell when the input is invalid(supported in only few terminals)

### Design Decisions
- **Item.java** is an abstract class and other items like weapons, armors extend an item.
- Visitor patter is used to apply/equip an item by an Hero
- Strategy pattern is used to change how monsters and heroes fight
- Factories are used at multiple places, like generating the world etc.
- *Composition over inheritance* is utilized at a few places.
- *Separation of concerns* is important so each responsibily is encapsulated by different components like Hero, Party, Item etc. Even IO operations are handled in its own class.


## How to compile and run
---------------------------------------------------------------------------
Directions on how to run the code.
```
javac App.java && java App
```

## Caveats
---------------------------------------------------------------------------
- Spells are not implemented
- Game is not balanced at all
- Currently cannot unequip an item

## UML
---------------------------------------------------------------------------
![plot](./legends_uml.png)


## Input/Output Example
---------------------------------------------------------------------------
```
generating a random world and heroes
This is the World
H: heros
M: market
W: wall
+--+--+--+--+--+--+--+--+
| H|  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  | M|  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
| W|  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
------------- Heroes and their information ----------------
Name         : Undefeated Yoj
Type         : WARRIOR
level        : 1
experience   : 7
HP           : 100.0
MP           : 400.0
Strength     : 2000.0
Dexterity    : 400.0
Agility      : 700.0
Gold         : 2500
------------- Heroes and their information ----------------
Name         : Skoraeus Stonebones
Type         : PALADIN
level        : 1
experience   : 4
HP           : 100.0
MP           : 250.0
Strength     : 2000.0
Dexterity    : 600.0
Agility      : 350.0
Gold         : 2500
------------- Heroes and their information ----------------
Name         : Rillifane Rallathil
Type         : SORCEROR
level        : 1
experience   : 9
HP           : 100.0
MP           : 1300.0
Strength     : 2000.0
Dexterity    : 450.0
Agility      : 500.0
Gold         : 2500
[+] Move(w/a/s/d/q/i/m/p/v/h): 
>> s
fight started with the monsters
Showing all monsters
------------------------------
name     : BunsenBurner
type     : DRAGON
level    : 4
HP       : 100.0
damage   : 40.0
defense  : 500.0
dodge    : 45.0
------------------------------
name     : Brandobaris
type     : DRAGON
level    : 3
HP       : 100.0
damage   : 35.0
defense  : 450.0
dodge    : 30.0
the following monsters are alive: BunsenBurner,Brandobaris
it is Undefeated Yoj's turn
[+] Do you want to see their stats?(y/n)
>> n
[+] which monster do you want to attact?
>> BunsenBurner
monster BunsenBurner has taken 100.0 damage from the hero Undefeated Yoj
monster BunsenBurner has died
the following monsters are alive: Brandobaris
it is Skoraeus Stonebones's turn
[+] Do you want to see their stats?(y/n)
>> n
[+] which monster do you want to attact?
>> BunsenBurner
not a valid input or monster is dead
[+] which monster do you want to attact?
>> n
not a valid input or monster is dead
[+] which monster do you want to attact?
>> Brandobaris
monster Brandobaris has dodged the attack
the following monsters are alive: Brandobaris
it is Rillifane Rallathil's turn
[+] Do you want to see their stats?(y/n)
>> n
[+] which monster do you want to attact?
>> Brandobaris
monster Brandobaris has dodged the attack
hero Undefeated Yoj has taken 35.0 damage from the monster Brandobaris
the following monsters are alive: Brandobaris
it is Undefeated Yoj's turn
[+] Do you want to see their stats?(y/n)
>> n
[+] which monster do you want to attact?
>> Brandobaris
monster Brandobaris has taken 100.0 damage from the hero Undefeated Yoj
monster Brandobaris has died
All monsters are dead
[+] Move(w/a/s/d/q/i/m/p/v/h): 
>> s
not a valid direction to move(encountered a wall)
[+] Move(w/a/s/d/q/i/m/p/v/h): 
>> d
[+] Move(w/a/s/d/q/i/m/p/v/h): 
>> p
This is the World
H: heros
M: market
W: wall
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |HM|  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
| W|  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+
[+] Move(w/a/s/d/q/i/m/p/v/h): 
>> m
marker has the following items and prices
Bow : 300.0
Mermaid Tears : 850.0
Breast plate : 350.0
Wizard Shield : 1200.0
Axe : 550.0
Full Body Armor : 1000.0
Scythe : 1000.0
Luck Elixir : 500.0
Platinum Shield : 150.0
TSwords : 1400.0
Strength Potion : 200.0
Sword : 500.0
Guardian Angel : 1000.0
Dagger : 200.0
Healing Potion : 250.0
Magic Potion : 350.0
heroes are: Undefeated Yoj,Skoraeus Stonebones,Rillifane Rallathil
[+] for which hero do you want to sell or buy(q to quit):
>> Skoraeus Stonebones
Skoraeus Stonebones has 2500 gold
[+] do you want to buy any item?(y/n)
>> y
[+] which item do you want to buy?
>> TSwords
[+] do you want to sell any item?(y/n)
>> n
[+] for which hero do you want to sell or buy(q to quit):
>> q
[+] Move(w/a/s/d/q/i/m/p/v/h): 
>> v
Heroes: Undefeated Yoj, Skoraeus Stonebones, Rillifane Rallathil
[+] select a hero to show their inventory
>> Skoraeus Stonebones
inventory for the hero: Skoraeus Stonebones
name:TSwords type:WEAPON
[+] item to use(q to quit):
>> TSwords
[+] Move(w/a/s/d/q/i/m/p/v/h): 
>> i
------------- Heroes and their information ----------------
Name         : Undefeated Yoj
Type         : WARRIOR
level        : 1
experience   : 7
HP           : 286.65
MP           : 400.0
Strength     : 2000.0
Dexterity    : 400.0
Agility      : 700.0
Gold         : 2500
------------- Heroes and their information ----------------
Name         : Skoraeus Stonebones
Type         : PALADIN
level        : 1
experience   : 4
HP           : 441.0
MP           : 250.0
Strength     : 2000.0
Dexterity    : 600.0
Agility      : 350.0
Gold         : 1100
------------- Heroes and their information ----------------
Name         : Rillifane Rallathil
Type         : SORCEROR
level        : 1
experience   : 9
HP           : 441.0
MP           : 1300.0
Strength     : 2000.0
Dexterity    : 450.0
Agility      : 500.0
Gold         : 2500
[+] Move(w/a/s/d/q/i/m/p/v/h): 
>> v
Heroes: Undefeated Yoj, Skoraeus Stonebones, Rillifane Rallathil
[+] select a hero to show their inventory
>> Skoraeus Stonebones
inventory for the hero: Skoraeus Stonebones
name:TSwords type:WEAPON (Equipped)
[+] item to use(q to quit):
>> q
[+] Move(w/a/s/d/q/i/m/p/v/h): 
>> d
fight started with the monsters
Showing all monsters
------------------------------
name     : Chrysophylax
type     : DRAGON
level    : 2
HP       : 100.0
damage   : 20.0
defense  : 500.0
dodge    : 20.0
------------------------------
name     : BunsenBurner
type     : DRAGON
level    : 4
HP       : 0.0
damage   : 40.0
defense  : 500.0
dodge    : 45.0
the following monsters are alive: Chrysophylax
it is Undefeated Yoj's turn
[+] Do you want to see their stats?(y/n)
>> n
[+] which monster do you want to attact?
>> Chrysophylax
monster Chrysophylax has taken 100.0 damage from the hero Undefeated Yoj
monster Chrysophylax has died
All monsters are dead
[+] Move(w/a/s/d/q/i/m/p/v/h): 
>> q
```