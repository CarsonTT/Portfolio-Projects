package labs.lab9;

public class Pair {
    private Integer yes;
    private Integer no;

    public Pair(Integer first, Integer second) {
        this.yes = first;
        this.no = second;
    }

    public Integer getYes() {
        return yes;
    }

    public void setYes() {
        this.yes ++;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo() {
        this.no ++;
    }

 
}
