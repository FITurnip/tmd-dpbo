package ViewModel;

import Model.PlayerScore;
import Model.TScore;

public class StartMenuViewModel {
    private TScore tScore;

    public StartMenuViewModel() {
        updateTScore();
    }

    public TScore gettScore() {
        return tScore;
    }

    public void settScore(TScore tScore) {
        this.tScore = tScore;
    }

    public void updateTScore() {
        tScore = new TScore();
    }
}
