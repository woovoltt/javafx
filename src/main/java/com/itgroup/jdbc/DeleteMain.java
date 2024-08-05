package com.itgroup.jdbc;

import com.itgroup.dao.KaraokeDao;

import java.util.Scanner;

public class DeleteMain {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("삭제할 상품 번호 : ");
        int id = scan.nextInt();

        KaraokeDao dao = new KaraokeDao();
        int cnt = -1;
        cnt = dao.deleteData(id);

        if (cnt == -1){
            System.out.println("상품 삭제에 실패하였습니다.");
        }else {
            System.out.println("상품 삭제에 성공하였습니다.");
        }
    }
}
