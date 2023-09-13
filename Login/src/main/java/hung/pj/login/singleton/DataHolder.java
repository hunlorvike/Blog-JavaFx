package hung.pj.login.singleton;

import java.util.ArrayList;
import java.util.List;

public class DataHolder<T> {
    private static DataHolder instance;
    private String data; // Dữ liệu kiểu String (giữ nguyên)

    // Thêm danh sách đối tượng kiểu T
    private List<T> dataList;

    private DataHolder() {
        dataList = new ArrayList<>();
    }

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

    // Thêm phương thức để thêm và lấy danh sách đối tượng kiểu T
    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}


