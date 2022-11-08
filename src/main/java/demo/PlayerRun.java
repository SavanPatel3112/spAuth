package demo;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class PlayerRun {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Player> playerList = new ArrayList<>();

        for (int i=0; i<4 ;i++) {
            int playerId = scanner.nextInt();
            String skill = scanner.next();
            String level = scanner.next();
            int points = scanner.nextInt();

            playerList.add(new Player(playerId,skill,level,points));
            scanner.nextLine();
        }

        String skill = scanner.next();
        int sum = findPointsForGivenSkill(playerList,skill);
        if (sum == 0){
            log.info("The given Skill is not available");
        }else {
            log.info("sum:{}",sum);
        }
        String level = scanner.next();
        Player player = getPlayerBasedOnLevel(playerList,level,skill);
        if (player==null){
            log.info("No player is available with specified level, skill and eligibility points");
        }else {
            log.info("player:{}",player.getPlayerId());
        }
    }


    public static int findPointsForGivenSkill (List<Player> players, String skill) {
        int sum = 0;
        for (Player player : players) {
            if (player.getSkill().equalsIgnoreCase(skill)){
                    sum = sum+player.getPoints();
                }
            }
        return sum;
    }

    public static Player getPlayerBasedOnLevel  (List<Player> playerList, String level, String skill) {
        Player players = null;
        if (playerList !=null) {
            for (Player player : playerList) {
                if (player.getPoints() > 20 && player.getLevel().equalsIgnoreCase(level) && player.getSkill().equalsIgnoreCase(skill)) {
                    players =player;
                }
            }
        }
        return players;
    }
}
