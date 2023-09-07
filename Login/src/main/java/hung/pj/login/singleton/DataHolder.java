package hung.pj.login.singleton;

public class DataHolder {
    private static DataHolder instance;
    private String data; // Thay đổi kiểu dữ liệu thành kiểu dữ liệu bạn muốn truyền

    private DataHolder() {}

    public static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

