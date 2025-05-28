package coffeepos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Menu {
    // 메뉴 번호, {메뉴 이름, {가격, 수량}}
    public static HashMap<Integer, HashMap<String, ArrayList<Integer>>> drinkMenus = new HashMap<>();
    public static HashMap<Integer, HashMap<String, ArrayList<Integer>>> desertMenus = new HashMap<>();

    static String[] drinks = {"아메리카노", "카페라떼", "바닐라라떼", "돌체라떼", "레몬에이드", "자몽에이드", "오레오스무디", "딸기스무디", "수박주스"};
    int[] drinksPrice = {2000, 2800, 3500, 3600, 3200, 3200, 3800, 4000, 4200};
    int[] drinksNum = {4, 1, 2, 3, 2, 5, 0, 3, 3};

    static String[] deserts = {"크로플", "티라미수", "스모어쿠키"};
    int[] desertsPrice = {2200, 4500, 2500};
    int[] desertsNum = {4, 2, 6};

    public Menu() { // 메뉴 초기화 해주는 용도
        int idx = 1;
        for(int i = 0; i < drinks.length; i++) {
            ArrayList<Integer> drinksList = new ArrayList<>();
            drinksList.add(drinksPrice[i]);
            drinksList.add(drinksNum[i]);

            HashMap<String, ArrayList<Integer>> maps = new HashMap<>();
            maps.put(drinks[i], drinksList);
            drinkMenus.put(idx, maps);
            idx++;
        }

        for(int i = 0; i < deserts.length; i++) {
            ArrayList<Integer> desertList = new ArrayList<>();
            desertList.add(desertsPrice[i]);
            desertList.add(desertsNum[i]);

            HashMap<String, ArrayList<Integer>> maps = new HashMap<>();
            maps.put(deserts[i], desertList);
            desertMenus.put(idx, maps);
            idx++;
        }
    }

    // 메뉴판(?) 출력
    public static int printMenu() {
        System.out.println("\n====== 주문 가능한 메뉴 ======");
        System.out.println("         < 음료 >");
        int idx = 1;
        for(int i = 0; i < drinks.length; i++) {   // 메뉴 이름 가져오기
            System.out.print("  " +idx +". " +drinks[i] +" - " +drinkMenus.get(idx).get(drinks[i]).getFirst());  // 이름, 가격 출력
            if(drinkMenus.get(idx).get(drinks[i]).getLast() == 0) {   // 수량이 0이면
                System.out.print("  (품절)");     // 품절 띄우기
            }
            System.out.println();
            idx++;
        }
        System.out.println("\n        < 디저트 >");
        for(int i = 0; i < deserts.length; i++) {
            System.out.print("  " +idx +". " +deserts[i] +" - " +desertMenus.get(idx).get(deserts[i]).getFirst());  // 이름, 가격 출력
            if(desertMenus.get(idx).get(deserts[i]).getLast() == 0) {   // 수량이 0이면
                System.out.print("  (품절)");     // 품절 띄우기
            }
            System.out.println();
            idx++;
        }
        System.out.println("          (p) 결제   (e) 종료");
        System.out.println("============================");

        int check = choiceMenu();   // 메뉴 선택으로 넘어가기
        return check;
    }

    // 메뉴 고르기
    public static int choiceMenu() {
        Scanner sc = new Scanner(System.in);
        HashMap<String, ArrayList<Integer>> choiceList = new HashMap<>();  // 사는 메뉴 이름, {가격, 갯수}
        String choiceStr;
        int sum = 0;    // 가격 추가

        while(true) {
            System.out.print("   >> ");
            choiceStr = sc.next();  // 계속 선택

            if(choiceStr.equals("e") || choiceStr.equals("p")) {
                break;
            }
            int choice = Integer.parseInt(choiceStr);
            if((choice >= 1 && choice <= 9)) {   // 수량이 0이 아니면
                HashMap<String, ArrayList<Integer>> menuMap = drinkMenus.get(choice);
                String menuName = null;
                ArrayList<Integer> menuInfo = null;

                for(HashMap.Entry<String, ArrayList<Integer>> entry : menuMap.entrySet()) {
                    menuName = entry.getKey();
                    menuInfo = entry.getValue();
                }
                if(menuInfo.getLast() == 0) {
                    System.out.println("\n수량이 없습니다. 다른 메뉴를 골라주세요:)");
                    continue;
                }
                sum += menuInfo.getFirst();  // 가격 더하기
                drinkMenus.get(choice).get(menuName).set(1, drinkMenus.get(choice).get(menuName).getLast() - 1); // 실제 수량 줄이고

                ArrayList<Integer> cur;
                if(choiceList.containsKey(menuName)) {
                    cur = choiceList.get(menuName);
                    cur.set(1, cur.get(1) + 1); // 수량만 +1
                }
                else {
                    cur = new ArrayList<>();
                    cur.add(menuInfo.getFirst());    // 가격 저장
                    cur.add(1);
                }
                choiceList.put(menuName, cur);   // 살 메뉴에 수량 카운트
            }
            else if((choice >= 10 && choice <= 12)) {
                HashMap<String, ArrayList<Integer>> menuMap = desertMenus.get(choice);
                String menuName = null;
                ArrayList<Integer> menuInfo = null;

                for (HashMap.Entry<String, ArrayList<Integer>> entry : menuMap.entrySet()) {
                    menuName = entry.getKey();
                    menuInfo = entry.getValue();
                }
                if(menuInfo.getLast() == 0) {
                    System.out.println("\n수량이 없습니다. 다른 메뉴를 골라주세요:)");
                    continue;
                }
                sum += menuInfo.getFirst();
                desertMenus.get(choice).get(menuName).set(1, desertMenus.get(choice).get(menuName).getLast() - 1);

                ArrayList<Integer> cur;
                if(choiceList.containsKey(menuName)) {
                    cur = choiceList.get(menuName);
                    cur.set(1, cur.get(1) + 1); // 수량만 +1
                }
                else {
                    cur = new ArrayList<>();
                    cur.add(menuInfo.getFirst());    // 가격 저장
                    cur.add(1);
                }
                choiceList.put(menuName, cur);   // 살 메뉴에 수량 카운트
            }
            System.out.print("현재 금액: " +sum);
            System.out.println();
        }

        if (choiceStr.equals("p")) {
            Payment.paymentPrint(sum, choiceList);
        }

        return -1;
    }
}