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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class KaraokeUpdateController implements Initializable {
    @FXML private TextField fxmlId;
    @FXML private TextField fxmlSongname;
    @FXML private TextField fxmlSinger;
    @FXML private ComboBox<String> fxmlGenre;
    @FXML private DatePicker fxmlSongdate;
    @FXML private TextField fxmlUrl;


    private Karaoke oldBean = null; // 수정 될 행의 정보
    private Karaoke newBean = null; // 데이터베이스에 수정할 Bean 객체

    public void setBean(Karaoke bean) {
        this.oldBean = bean;

        fillPreviousData();

        // 데이터베이스의 primary key에 해당하는 상품 번호를 숨겨줍니다.
        fxmlId.setVisible(false);

    }

    private void fillPreviousData() {
        // 과거에 내가 등록했던 정보들을 해당 컨트롤러에 다시 입력해줍니다.
        fxmlId.setText(String.valueOf(this.oldBean.getId()));
        fxmlSongname.setText(this.oldBean.getSongname());
        fxmlSinger.setText(this.oldBean.getSinger());

        String genre = this.oldBean.getGenre();
        fxmlGenre.setValue(Utility.getCategoryName(genre, "key"));

        String songDate = this.oldBean.getSongdate();
        if (songDate == null || songDate.equals("null")) {

        } else {
            fxmlSongdate.setValue(Utility.getDatePicker(songDate));
        }
        fxmlUrl.setText(this.oldBean.getUrl());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("수정 오네가이시마스");
    }

    public void onKaraokeUpdate(ActionEvent event) {
        // 먼저 유효성 검사를 진행합니다.
        boolean bool = validationCheck();

        // 사용자가 변경한 내역을 데이터베이스에 업데이트 시킵니다.
        if (bool == true){
            KaraokeDao dao = new KaraokeDao();
            int cnt = -1; // -1이면 실패
            cnt = dao.updateData(this.newBean);
            if (cnt == -1){
                System.out.println("수정 실패");
            }else { // 수정이 되었으므로 창을 닫습니다.
                Node source = (Node)event.getSource();
                Stage stage = (Stage)source.getScene().getWindow();
                stage.close();
            }
        }else {
            System.out.println("유효성 검사를 통과하지 못했습니다.");
        }

    }

    private boolean validationCheck() {
        // 유효성 검사를 통과하면 true가 됩니다.

        // 수정을 위한 핵심 키(primary key)
        int id = Integer.valueOf(fxmlId.getText().trim());

        String[] message = null;

        String songname = fxmlSongname.getText().trim();
        if (songname.length() <= 1 || songname.length() >= 100) {
            message = new String[]{"유효성 검사 : 노래제목", "길이 제한 위배", "노래제목은 1글자 이상 100글자 이하이어야 합니다."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        }

        String singer = fxmlSinger.getText().trim();
        if (singer.length() <= 1 || singer.length() >= 100) {
            message = new String[]{"유효성 검사 : 가수명", "길이 제한 위배", "가수명은 1글자 이상 100글자 이하이어야 합니다."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        }


        // 주의) 입력 상자 객체는 not null이지만 데이터 입력이 되어있지 않는 경우 getText() 메소드는 null을 반환합니다.
//        System.out.println("fxmlImage02 == null");
//        System.out.println(fxmlImage02 == null);
//
//        System.out.println("fxmlImage02.getText() == null");
//        System.out.println(fxmlImage02.getText() == null);


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
        String songDate = null;
        if (_songDate == null) {
            message = new String[]{"유효성 검사 : 입고일자", "무효한 날짜 형식", "올바른 입고 일자 형식을 입력해주세요."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        } else {
            songDate = _songDate.toString();
            songDate = songDate.replace("-", "/");
        }

        boolean bool = false;
        String url = null ;
        if(fxmlUrl.getText() != null && fxmlUrl.getText().length() != 0){
            url = fxmlUrl.getText().trim();

            bool = url.endsWith("https://");
            if(!bool){
                message = new String[]{"유효성 검사 : Url", "Url 형식 점검", "Url은  'https://'으로 시작해야 합니다."} ;
                Utility.showAlert(Alert.AlertType.WARNING, message);
                return false;
            }
        }

        // 유효성 검사가 통과되면 비로소 객체 생성 합니다.
        this.newBean = new Karaoke();

        newBean.setId(id); // 중요) 이 상품번호를 근거로 수정이 됩니다.
        newBean.setSongname(songname);
        newBean.setSinger(singer);
        newBean.setGenre(Utility.getCategoryName(genre, "value"));
        newBean.setSongdate(songDate);
        newBean.setUrl(url);


        return true;
    }

}
