package com.itgroup.jdbc;

import com.itgroup.bean.Karaoke;
import com.itgroup.dao.KaraokeDao;

public class InsertMain {
    public static void main(String[] args) {
        // 관리자가 상품 1개를 등록합니다.
        KaraokeDao dao = new KaraokeDao();
        Karaoke bean = new Karaoke();

        bean.setId(bean.getId());
        bean.setSongname(bean.getSongname());
        bean.setSinger(bean.getSinger());
        bean.setGenre(bean.getGenre());
        bean.setSongdate(bean.getSongdate());

        int cnt = -1; // -1을 실패한 경우라고 가정합니다.
        cnt = dao.insertData(bean);

        if (cnt == -1){
            System.out.println("상품 등록에 실패하였습니다.");
        }else {
            System.out.println("상품 등록에 성공하였습니다.");
        }
    }
}
