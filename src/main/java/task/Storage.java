package task;

public class Storage {
    private int stock = 1_000;

    public int takeFromStock(int amount) {
        int res = amount > getStock() ? getStock() : amount;
        setStock(getStock() - res);
        return res;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int newVal) {
        stock = newVal >= 0 ? newVal : stock;
    }
}
