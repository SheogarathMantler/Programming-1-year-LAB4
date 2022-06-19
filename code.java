import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws NoNameArticleException{
        Magazine magazine1 = new Magazine("Для любителей почитать лежа");
        Magazine magazine2 = new Magazine("Давилонские побасенки");
        Magazine somemagazine = new Magazine("");
        Person gadkinz = new Person("Гадкинз");
        ActionedPerson spruts = new ActionedPerson("Спрутс", "Спрутс протягивает щупальца к владельцам гигантских акций");
        ActionedPerson gorloderiki = new ActionedPerson("Горлодерики", " покупают акции"){
            boolean isbuying = false;
            @Override
            public void action(){
                this.isbuying = true;
                System.out.println("горлодерики покупают акции");
            }
        };
        ActionedPerson skuperfield = new ActionedPerson("Скуперфильд", " пугается подъёма цен и отдает приказ"){
            @Override
            public void action(){
                if (Stocks.intValue >= 60){
                    System.out.println(name + action);
                    gorloderiki.action();
                }
            }

        };
        gadkinz.setProperty("газета 'Для любителей почитать лежа'");
        gadkinz.setProperty("газета 'Давилонские побасенки'");
        gadkinz.setProperty("еще одна газета");
        spruts.setProperty("газета 'Давилонские юморески'");
        ActionedPerson owners = new ActionedPerson("владельцы акций", "Владельцы акций боятся"){
            final String action1 = "Владельцы акций успокоились";
            @Override
            public void action(){
                if (Stocks.intValue > 20) {
                    System.out.println(action1);
                } else {
                    System.out.println(action);
                }
            }
        };
        Stocks giantStocks = new Stocks("гигантские акции");
        owners.setProperty(giantStocks.getName());
        Market market = new Market("Давилонская биржа");
        magazine2.setArticle("Паника на давилонской бирже");
        owners.getProperty();
        gadkinz.getProperty();
        giantStocks.getPrice();
        spruts.action();
        magazine1.setArticle("Берегите карманы!");
        owners.action();
        market.action();
        try {
            giantStocks.setPrice(Stocks.price.SECOND);
            owners.action();
            somemagazine.setArticle("Куда тянутся щупальца Спрутса?");
            giantStocks.setPrice(Stocks.price.THIRD);
            skuperfield.action();
            giantStocks.setPrice(Stocks.price.FOURTH);
            System.out.println("Прошел день");
            giantStocks.setPrice(Stocks.price.FIFTH);
        } catch (LowStocksPriceException exception) {
            System.out.println(exception.getMessage());
        }

    }
}


interface actionable {
    void action();
}
interface nameable {
    String getName();
}

abstract class Subject implements nameable{
    protected String name;
    public String getName(){
        return this.name;
    }
    @Override
    public String toString(){
        return this.getClass() + " " + this.name;
    }
}

class Stocks extends Subject {
    enum price {BEGIN, SECOND, THIRD, FOURTH, FIFTH}
    private price value = price.BEGIN;
    static int intValue = 5;
    static boolean isInfluenced = false;
    public Stocks(String name){
        this.name = name;
    }
    public void getPrice() {
        System.out.println("Цена акций сейчас " + intValue);
    }
    public void setPrice(price price) throws LowStocksPriceException{
        this.value = price;
        if (price == Stocks.price.SECOND) {
            intValue = 50;
        }
        if (price == Stocks.price.THIRD & isInfluenced) {
            intValue = 60;
        }
        if (price == Stocks.price.FOURTH & isInfluenced) {
            intValue = 70;
        }
        if (price == Stocks.price.FIFTH & isInfluenced) {
            intValue = 80;
        }
        System.out.println("Акции теперь стоят " + intValue);
        if (intValue < 5){
            throw new LowStocksPriceException("Владельцы акций разорены!");
        }
    }
    @Override
    public int hashCode(){
        return this.intValue;
    }
    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()){
            Stocks other = (Stocks) obj;
            return this.value == other.value;
        } else { return false; }
    }
}
class Person extends Subject {
    private String property = "nothing";
    Person(String name){
        this.name = name;
    }
    public String getProperty(){
        System.out.println(this.name + " владеет " + this.property);
        return this.property;
    }
    public void setProperty(String nameOfProperty){
        if (property.equals("nothing")) {
            this.property = nameOfProperty;
        } else {
            this.property = this.property + nameOfProperty;
        }
    }
    @Override
    public int hashCode(){
        return name.length();
    }
    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()){
            Person other = (Person) obj;
            return this.name.equals(other.name);
        } else { return false; }
    }
}

class ActionedPerson extends Person implements actionable{
    final String action;
    ActionedPerson(String name, String action) {
        super(name);
        this.action = action;
    }
    public void action(){
        System.out.println(action);
    }
}

class Magazine extends Subject {
    class Impact {
        String message;
        Impact(String name) {
            this.message = "происходит влияние статьи " + name;
        }
        public void influence(){
            Stocks.isInfluenced = true;
            System.out.println(message);
        }
    }
    private ArrayList<String> articles = new ArrayList<>();
    public Magazine(String name){
        this.name = name;
    }

    public void setArticle(String nameOfArticle){
        this.articles.add(nameOfArticle);
        System.out.println("В газете '" + this.name + "' вышла статья '" + nameOfArticle + "'");
        Impact impact = new Impact(nameOfArticle);
        if (Stocks.intValue >= 50){
            impact.influence();
        }
    }
}

class Market extends Subject implements actionable{
    Market(String name){
        this.name = name;
    }
    public void action(){
        System.out.println("Биржа открылась!");
    }
}
class LowStocksPriceException extends Exception {          // checked
    public LowStocksPriceException(String message){
        super(message);
    }
}
class NoNameArticleException extends RuntimeException {    // unchecked
    String message;
    public NoNameArticleException(){
        this.message = "У статьи нет названия!";
    }
}
