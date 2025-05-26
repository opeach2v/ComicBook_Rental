package coffeepos;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        Payment.initPriceByMenu();  // 초기화

        int check = mainPrint();

        if(check == -1) {
            System.exit(0);
        }
    }

    // 메인 시스템 출력
    public static int mainPrint() {
        Scanner sc = new Scanner(System.in);
        System.out.println("====== 슬기's 카페 POS 시스템 ======");
        System.out.println("   1. 주문 하기");
        System.out.println("   2. 관리자 모드 접속");
        System.out.println("   3. 프로그램 종료");

        int choice = 0;
        int check = 0;

        while (choice != 1 && choice != 2 && choice != 3) {
            System.out.print("     > ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    check = Menu.printMenu();
                    break;
                case 2:
                    Manager.managerPrint();
                    break;
                case 3:
                    check = -1;
                default:
                    System.out.println("  1 ~ 3번만 입력해주세요!!");
                    break;
            }
        }
        return check;
    }
}
