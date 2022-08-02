package kr.co.goplan.mtgame.constant;

public enum ContentsType {
    Video(0), Image(1), Text(2), Audio(3), TagLine(4), ETC(5),
    HTML(6), Url(7), PDF(8), PPT(9), Undefined(10), App(11),
    Thumbnail(12),ALL(13);

    private int value;

    ContentsType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
