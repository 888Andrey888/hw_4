import java.util.Random;

public class Main {
    public static int bossHealth = 1000;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {280, 270, 250, 300, 1000, 230, 250, 250};
    public static int[] heroesDamage = {10, 15, 20, 20, 2, 15, 10, 10};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Berserk", "Thor"};
    public static int roundNumber = 0;
    public static Random random = new Random();
    public static int berserkBlok;
    public static boolean bossStun = false;


    public static void main(String[] args) {
        printStatistics();
        while (!isGameFinished()) {
            playRound();
        }
    }

    public static void chooseBossDefence() {
        int randomIndex = random.nextInt(heroesAttackType.length); // 0,1,2
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        healHero();
        bossHits();
        heroesHit();
        printStatistics();
    }

    public static void healHero() {
        for (int i = 0; i < heroesAttackType.length; i++) {
            int heal = heroesDamage[i];
            if (heroesAttackType[i].equals("Medic")) {
                if (heroesHealth[i] > 0) {
                    boolean healed = false;
                    for (int j = 0; j < heroesHealth.length; j++) {
                        boolean toHeal = random.nextBoolean();
                        if (heroesHealth[j] < 100 && heroesHealth[j] > 0 && toHeal && !heroesAttackType[j].equals("Medic")) {
                            if (bossDefence == heroesAttackType[i]) {
                                int coeff = random.nextInt(2, 5);
                                heal = heal * coeff;
                                System.out.println("Critical heal: " + heal);
                            }
                            heroesHealth[j] += heal;
                            System.out.println(heroesAttackType[j] + " added health " + heal);
                            System.out.println(heroesAttackType[j] + " health: " + heroesHealth[j] + " damage: " + heroesDamage[j]);
                            healed = true;
                            break;
                        }
                    }
                    //данный блок кода добавил потому что предыдущая конструция может ни разу не выполниться. И тогда хелик принудительно вылечит первого попавшегося героя,
                    //у которого здоровие будет ниже 100 если таковые имются
                    if (!healed) {
                        for (int j = 0; j < heroesHealth.length; j++) {
                            if (heroesHealth[j] < 100 && heroesHealth[j] > 0 && !heroesAttackType[j].equals("Medic")) {
                                if (bossDefence == heroesAttackType[i]) {
                                    int coeff = random.nextInt(2, 5);
                                    heal = heal * coeff;
                                    System.out.println("Critical heal: " + heal);
                                }
                                heroesHealth[j] += heal;
                                System.out.println(heroesAttackType[j] + " added health " + heal);
                                System.out.println(heroesAttackType[j] + " health: " + heroesHealth[j] + " damage: " + heroesDamage[j]);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0 && !heroesAttackType[i].equals("Medic")) {
                int damage = heroesDamage[i];
                if (bossDefence == heroesAttackType[i]) {
                    int coeff = random.nextInt(8) + 2; // 2,3,4,5,6,7,8,9
                    damage = damage * coeff;
                    System.out.println("Critical damage: " + damage);
                }
                if (heroesAttackType[i].equals("Berserk")) {
                    damage += berserkBlok;
                    System.out.println(heroesAttackType[i] + " damage: " + damage);
                } else if (heroesAttackType[i].equals("Thor")) {
                    bossStun = random.nextBoolean();
                    if (bossStun) {
                        System.out.println(heroesAttackType[i] + " stuned boss");
                    }
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth -= damage;
                }
            }
        }
    }

    public static void bossHits() {
        if (!bossStun) {
            for (int i = 0; i < heroesHealth.length; i++) {
                int damage = bossDamage;
                if (heroesAttackType[i].equals("Lucky")) {
                    if (random.nextBoolean()) {
                        System.out.println(heroesAttackType[i] + " dodged a blow!");
                        continue;
                    }
                }
                if (heroesAttackType[i].equals("Golem")) {
                    damage = (damage + (damage / 5) * (heroesHealth.length - 1));
                } else if (heroesAttackType[i].equals("Berserk")) {
                    berserkBlok = random.nextInt(5, 20);
                    System.out.println(heroesAttackType[i] + " bloked: " + berserkBlok);
                    damage -= berserkBlok;
                } else {
                    boolean hesGolem = false;
                    for (int j = 0; j < heroesHealth.length; j++) {
                        if (heroesAttackType[j].equals("Golem")) {
                            hesGolem = true;
                            break;
                        }
                    }
                    if (hesGolem) {
                        damage -= damage / 5;
                    }
                }
                if (heroesHealth[i] > 0) {
                    if (heroesHealth[i] - bossDamage < 0) {
                        heroesHealth[i] = 0;
                    } else {
                        heroesHealth[i] -= damage;// heroesHealth[i] = heroesHealth[i] - bossDamage;
                    }
                }
            }
        }
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        /*if (heroesHealth[0] <= 0 && heroesHealth[1] <= 0 && heroesHealth[2] <= 0) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;*/
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " ---------------");
        /*String defence;
        if (bossDefence == null) {
            defence = "No defence";
        } else {
            defence = bossDefence;
        }*/
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: "
                + (bossDefence == null ? "No defence" : bossDefence) + " status:" + (bossStun == true ? " stan" : " fights"));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i]
                    + " damage: " + heroesDamage[i]);
        }
    }
}
