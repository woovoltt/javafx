package com.itgroup.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Utility {
    public static final String FXML_PATH = "/com/itgroup/fxml/";
    public static final String IMAGE_PATH = "/com/itgroup/images/"; // 이미지 파일이 있는 경로

    private static Map<String, String> categoryMap = new HashMap<>();

    private static String makeMapData(String category, String mode) {
        // 사용자가 카테고리 선택 시 '한글'을 보고 선택하므로 key에 한글 이름이 들어가야 합니다.
        // 맵 자료 구조는 value를 이용하여 key 검색을 못합니다.

        // mode가 "key"(키 찾기), "value"(값 찾기)
        categoryMap.put("발라드", "발라드");
        categoryMap.put("힙합", "힙합");
        categoryMap.put("댄스", "댄스");
        categoryMap.put("락", "락");
        categoryMap.put("록", "록");

        if (mode.equals("value")){ // key로 value 찾기
            return categoryMap.get(category);

        }else { // value로 key 찾기
            for (String key : categoryMap.keySet()){
                if (categoryMap.get(key).equals(category)){
                    return key; // 값이 발견 되었으므로 key를 반환합니다.
                }
            }
            return null; // key가 없군요
        }

    }

    public static String getCategoryName(String category, String mode) {
        return makeMapData(category, mode);
    }

    public static void showAlert(Alert.AlertType alertType, String[] message) {
        // 단순 메세지 박스를 보여주기 위한 유틸리티 메소드입니다.
        Alert alert = new Alert(alertType);
        alert.setTitle(message[0]);
        alert.setHeaderText(message[1]);
        alert.setContentText(message[2]);
        alert.showAndWait();
    }

    public static LocalDate getDatePicker(String songDate) {
        // 문자열을 LocalDate 타입으로 변환하여 반환합니다.
        // 회원 가입 일자, 게시물 작성 일자, 상품 등록 일자 등에서 사용할 수 있습니다.
        System.out.println(songDate);
        try{
            int year = Integer.valueOf(songDate.substring(0, 4));
            int month = Integer.valueOf(songDate.substring(5, 7));
            int day = Integer.valueOf(songDate.substring(8));

            return LocalDate.of(year, month, day);
        }catch (NumberFormatException e){
            System.out.println("잘못된 날짜 형식: " + songDate);
            return null;
        }
    }
}
