package com.itgroup.controller;

import com.itgroup.bean.Karaoke;
import com.itgroup.dao.KaraokeDao;
import com.itgroup.utility.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class KaraokeInsertController implements Initializable {

    @FXML private TextField fxmlId;
    @FXML private TextField fxmlSongname;
    @FXML private TextField fxmlSinger;
    @FXML private ComboBox<String> fxmlGenre;
    @FXML private DatePicker fxmlSongdate;
    @FXML private TextField fxmlUrl;


    KaraokeDao dao = null;
    Karaoke bean = null; // 상품 1개를 의미하는 빈 클래스

    public void onProductInsert(ActionEvent event) {
        // 기입한 상품 목록을 데이터베이스에 추가합니다.
        // event 객체는 해당 이벤트를 발생시킨 객체입니다.
        System.out.println(event);
        boolean bool = validationCheck();
        if (bool == true) {
            int cnt = -1;
            cnt = insertDatabase();
            if (cnt == 1){ // 인서트 성공 시
                Node source = (Node)event.getSource(); // 강등
                Stage stage = (Stage)source.getScene().getWindow(); // 강등
                stage.close(); // 현재 창을 닫습니다.
            }
        } else {
            System.out.println("등록 실패");
        }
    }

    private int insertDatabase() {
        // 1건의 데이터인 bean을 dao를 사용하여 데이터베이스에 추가합니다.
        int cnt = -1; // 작업 실패
        cnt = dao.insertData(this.bean);
        if (cnt == -1){
            String[] message = new String[]{"노래 등록", "노래 등록 실패", "노래 등록을 실패하였습니다."};
            Utility.showAlert(Alert.AlertType.ERROR, message);
        }
        return cnt;
    }

    private boolean validationCheck() {
        // 유효성 검사를 통과하면 true가 됩니다.
        String[] message = null;

        int Id = 0;
        try {
            String _price = fxmlId.getText().trim();
            Id = Integer.valueOf(_price);

            if (Id <= 1 || Id >= 1000) {
                message = new String[]{"유효성 검사 : 아이디", "허용 숫자 위반", "아이디는 1 이상 1000 이하로 입력해주세요."};
                Utility.showAlert(Alert.AlertType.WARNING, message);
                return false;
            }

        } catch (NumberFormatException ex) {
            ex.printStackTrace();

            message = new String[]{"유효성 검사 : 아이디", "무효한 숫자 형식", "올바른 숫자 형식을 입력해주세요."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        }

        String songname = fxmlSongname.getText().trim();
        if (songname.length() <= 1 || songname.length() >= 100) {
            message = new String[]{"유효성 검사 : 노래제목", "길이 제한 위배", "노래제목은 1글자 이상 100글자 이하이어야 합니다."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        }

        String singer = fxmlSinger.getText().trim();
        if (singer.length() <= 1 || singer.length() >= 100) {
            message =  new String[]{"유효성 검사 : 가수", "길이 제한 위배", "가수명은 1글자 이상 100글자 이하이어야 합니다."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        }

        int selectedIndex = fxmlGenre.getSelectionModel().getSelectedIndex();
        String genre = null;
        if (selectedIndex == 0) {
            message = new String[]{"유효성 검사 : 장르", "장르 미선택", "원하시는 장르를 반드시 선택해주세요."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        } else {
            genre = fxmlGenre.getSelectionModel().getSelectedItem();
            System.out.println("선택된 장르");
            System.out.println(genre);
        }

        LocalDate _songDate = fxmlSongdate.getValue();
        String songdate = null;
        if (_songDate == null) {
            message = new String[]{"유효성 검사 : 발매일", "무효한 날짜 형식", "올바른 날짜 형식을 입력해주세요."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        } else {
            songdate = _songDate.toString();
            songdate = songdate.replace("-", "/");
        }

        boolean bool = false;
        String url = null ;
        if(fxmlUrl.getText() != null && fxmlUrl.getText().length() != 0){
            url = fxmlUrl.getText().trim();

            bool = url.endsWith("https://");
            if(!bool){
                message = new String[]{"유효성 검사 : Url", "Url 형식 점검", "Url은 'https://'으로 시작해야 합니다."} ;
                Utility.showAlert(Alert.AlertType.WARNING, message);
                return false;
            }
        }


        // 유효성 검사가 통과되면 비로소 객체 생성 합니다.
        this.bean = new Karaoke();

        bean.setId(Id);
        bean.setSongname(songname);
        bean.setSinger(singer);
        bean.setGenre(Utility.getCategoryName(genre, "value"));
        bean.setSongdate(songdate);

        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dao = new KaraokeDao();

        // 최초 시작 시 콤보 박스의 0번째 항목 선택하기
        fxmlGenre.getSelectionModel().select(0);
    }
}
