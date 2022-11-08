package demo;

public class Player {
    int playerId;
    String skill;
    String level;
    int points;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Player(int playerId, String skill, String level, int points) {
        this.playerId = playerId;
        this.skill = skill;
        this.level = level;
        this.points = points;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", skill='" + skill + '\'' +
                ", level='" + level + '\'' +
                ", points=" + points +
                '}';
    }
}
