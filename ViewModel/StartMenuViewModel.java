package ViewModel;

import Model.TScore;

public class StartMenuViewModel {
    // TScore Property
    private TScore tScore;

    public StartMenuViewModel() {
        // init val of tscore
        updateTScore();
    }

    /**
     * Setter and getter
     */
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
