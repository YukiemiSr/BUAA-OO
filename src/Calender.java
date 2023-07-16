public class Calender {
    private Integer month;
    private Integer day;
    private Integer sum;
    private Integer cnt;

    public Calender() {
        this.month = 1;
        this.day = 1;
        this.sum = 0;
        this.cnt = 1;
    }

    public void tomorrow() {
        switch (month) {
            case 12:
                if (day == 31) {
                    return;
                } else {
                    day++;
                }
                break;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
                if (day == 31) {
                    day = 1;
                    month++;
                } else {
                    day++;
                }
                break;
            case 2:
                if (day == 28) {
                    day = 1;
                    month++;
                } else {
                    day++;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (day == 30) {
                    day = 1;
                    month++;
                } else {
                    day++;
                }
                break;
            default:
                break;
        }
        sum = (sum + 1) % 3;
        cnt++;
    }

    @Override
    public String toString() {
        String s1 = String.format("%02d", month);
        String s2 = String.format("%02d", day);
        return "[2023-" + s1 + "-" + s2 + "]";
    }

    public boolean tidyTime() {
        return sum == 0;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer i) {
        this.cnt = i;
    }
}
