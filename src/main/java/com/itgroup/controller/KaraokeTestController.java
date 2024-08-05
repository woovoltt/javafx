package com.itgroup.controller;

import com.itgroup.bean.Karaoke;
import com.itgroup.dao.KaraokeDao;
import com.itgroup.utility.Paging;
import com.itgroup.utility.Utility;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class KaraokeTestController implements Initializable {
    private KaraokeDao dao = null;
    private String mode = null;

    @FXML private TableView<Karaoke> karaokeTable;// 테이블 목록을 보여주는 뷰
    @FXML private ImageView imageView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dao = new KaraokeDao();

        setTabelColumns();
        setPagination(0);

        ChangeListener<Karaoke> tableListener = new ChangeListener<Karaoke>() {
            @Override
            public void changed(ObservableValue<? extends Karaoke> observableValue, Karaoke oldValue, Karaoke newValue) {
                if (newValue != null){
                    System.out.println("노래 정보");
                    System.out.println(newValue);

                    String imageFile = "";
                    if (newValue.getImage() == null){
                        imageFile = Utility.IMAGE_PATH + "karaoke.gif";
                    }else {

                    }

                    Image someImage = null;
                    if (getClass().getResource(imageFile) == null){
                        imageView.setImage(null);
                    }else {
                        someImage = new Image(getClass().getResource(imageFile).toString());
                        imageView.setImage(someImage);
                    }
                }
            }
        };
        karaokeTable.getSelectionModel().selectedItemProperty().addListener(tableListener);
    }

    private void setTabelColumns() {
        String[] fields = {"id", "songname", "singer", "genre", "songdate"};
        String[] colNames = {"아이디", "노래제목", "가수", "장르", "발매일"};

        TableColumn tableColumn = null;

        for (int i = 0; i < fields.length; i++) {
            tableColumn = karaokeTable.getColumns().get(i);
            tableColumn.setText(colNames[i]);
            tableColumn.setCellValueFactory(new PropertyValueFactory<>(fields[i]));
            tableColumn.setStyle("-fx-alignment:center;");
        }
    }

    public void onInsert(ActionEvent event) throws Exception{
        // 상품을 등록합니다.
        // fxml 파일 로딩
        String fxmlFile = Utility.FXML_PATH + "KaraokeInsert.fxml";
        // import java.net.URL;
        URL url = getClass().getResource(fxmlFile);

        FXMLLoader fxmlLoader = new FXMLLoader(url);

        Parent container = fxmlLoader.load(); // fxml의 최상위 컨테이너 객체

        Scene scene = new Scene(container); // 씬에 담기
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene); // 씬을 무대에 담기
        stage.setResizable(false);
        stage.setTitle("상품 등록하기");
        stage.showAndWait(); // 창 띄우고 대기

        setPagination(0); // 화면 갱신
    }

    @FXML private Pagination pagination;
    private void setPagination(int pageIndex) {
        // 페이징 관련 설정을 합니다.
        pagination.setCurrentPageIndex(pageIndex);
        pagination.setPageFactory(this::createPage);

        // 화면 갱신 시 이미지 뷰 정보도 없애주기
        // imageView.setImage(null);
    }

    private Node createPage(Integer pageIndex) {
        // 각 페이지의 Pagination을 동적으로 생성해주는 공장(Factory) 역할을 합니다.
        // mode 변수는 필드 검색 시 사용하는 변수입니다.

        int totalCount = 0;
        totalCount = dao.getTotalCount(this.mode);

        Paging pageInfo = new Paging(String.valueOf(pageIndex+1), "10", totalCount, null, this.mode, null);

        pagination.setPageCount(pageInfo.getTotalPage());

        fillTableData(pageInfo);

        VBox vbox = new VBox(karaokeTable);

        return vbox;
    }
    private ObservableList<Karaoke> dataList = null;

    private void fillTableData(Paging pageInfo) {
        // 테이블 뷰에 목록을 채워줍니다.

        List<Karaoke> productList = dao.getPaginationData(pageInfo);

        dataList = FXCollections.observableArrayList(productList);

        karaokeTable.setItems(dataList);

    }

    public void onDelete(ActionEvent actionEvent) {

        int idx = karaokeTable.getSelectionModel().getSelectedIndex();

        if (idx >= 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("삭제 확인 메세지");
            alert.setHeaderText("삭제 항목 선택 대화 상자");
            alert.setContentText("이 항목을 정말로 삭제하시겠습니까?");

            Optional<ButtonType> response = alert.showAndWait();

            if (response.get() == ButtonType.OK){
                Karaoke bean = karaokeTable.getSelectionModel().getSelectedItem();
                int id = bean.getId();
                int cnt = -1;
                cnt = dao.deleteData(id);

                if (cnt != -1){
                    System.out.println("삭제 성공");
                    setPagination(0);

                }else {
                    System.out.println("삭제 실패");
                }
            }else {
                System.out.println("삭제를 취소하였습니다.");
            }

        }else {
            String[] message = new String[]{"삭제할 노래 목록 확인", "삭제할 노래 미선택", "삭제할 노래를 선택해주세요."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
        }
    }

    public void onClosing(ActionEvent event) {
        System.out.println("프로그램을 종료합니다.");
        Platform.exit();
    }

//    @FXML private ComboBox<String> fieldSearch;
//
//    public void choiceSelect(ActionEvent event) {
//        KaraokeDao dao = new KaraokeDao();
//
//
//        // 특정 카테고리에 대하여 필터링 합니다.
//        String category = fieldSearch.getSelectionModel().getSelectedItem();
//        System.out.println("검색 카테고리 : [" + category + "]");
//
//        this.mode = Utility.getCategoryName(category, "value");
//        System.out.println("필드 검색 모드 : [" + this.mode + "]");
//
//        setPagination(0); // 화면 정보 갱신
//    }

    @FXML private ComboBox<String> fieldSearch;
    public void choiceSelect(ActionEvent event) {
        // 특정 카테고리에 대하여 필터링 합니다.
        String category = fieldSearch.getSelectionModel().getSelectedItem();
        System.out.println("검색 장르 : [" + category + "]");

        this.mode = Utility.getCategoryName(category, "value");
        System.out.println("필드 검색 모드 : [" + this.mode + "]");

        setPagination(0); // 화면 정보 갱신
    }


    public void linkGo(ActionEvent event) {
        KaraokeDao dao = new KaraokeDao();
        int idx = karaokeTable.getSelectionModel().getSelectedIndex();

        Desktop desktop = Desktop.getDesktop();
        if (idx >= 0){
            idx = karaokeTable.getSelectionModel().getSelectedItem().getId();
            String url = dao.getUrl(idx);
            try{
                desktop.browse(new URI(url));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            String[] message = new String[]{"바로가기 할 노래 확인", "링크를 보여줄 노래 미선택", "링크를 보여줄 노래를 선택해주세요."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
        }

    }

    public void onUpdate(ActionEvent event) throws Exception{

        int idx = karaokeTable.getSelectionModel().getSelectedIndex();
        System.out.println(idx);

        if (idx >= 0){
            System.out.println("선택된 색인 번호 : " + idx);

            String fxmlFile = Utility.FXML_PATH + "KaraokeUpdate.fxml";
            URL url = getClass().getResource(fxmlFile);
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent container = fxmlLoader.load();

            Karaoke bean = karaokeTable.getSelectionModel().getSelectedItem();

            KaraokeUpdateController controller = fxmlLoader.getController();

            controller.setBean(bean);

            Scene scene = new Scene(container);
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("노래 수정하기");
            stage.showAndWait();
            setPagination(0);

        }else {
            String[] message = new String[]{"노래 선택 확인", "노래 미선택", "수정 하고자 하는 노래를 선택해주세요."};
            Utility.showAlert(Alert.AlertType.ERROR, message);
        }
    }
}
