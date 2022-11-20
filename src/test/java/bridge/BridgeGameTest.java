package bridge;

import bridge.domain.Bridge;
import bridge.type.FinishCondition;
import bridge.type.GameStatus;
import bridge.type.PassCondition;
import bridge.type.ProcessCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class BridgeGameTest {

    Bridge bridge;
    GameStatusOperator gameStatusOperator;
    BridgeGame bridgeGame;

    @BeforeEach
    void initBridgeGame() {
        bridge = new Bridge(List.of("U", "D", "U", "D"));
        gameStatusOperator = GameStatusOperator.initGameStatusOperator();
        bridgeGame = new BridgeGame(bridge, gameStatusOperator);
    }

    @DisplayName("플레이어가 건널 수 없는 칸을 선택한 경우 FAIL을 반환한다.")
    @Test
    void moveNonPassableBlock() {
        ProcessCondition passCondition = bridgeGame.move("D");
        Integer currentPosition = gameStatusOperator.getCurrentPosition();
        assertThat(passCondition).isEqualTo(PassCondition.FAIL);
        assertThat(currentPosition).isEqualTo(-1);
    }

    @DisplayName("플레이어가 건널 수 있는 칸을 선택한 경우 PASS를 반환한다.")
    @Test
    void movePassableBlock() {
        ProcessCondition passCondition = bridgeGame.move("U");
        Integer currentPosition = gameStatusOperator.getCurrentPosition();
        assertThat(passCondition).isEqualTo(PassCondition.PASS);
        assertThat(currentPosition).isEqualTo(0);
    }

    @DisplayName("플레이어가 게임을 재시작한다")
    @Test
    void retry() {
        bridgeGame.retry();
        GameStatus gameStatus = gameStatusOperator.getGameStatus();
        assertThat(gameStatus).isEqualTo(GameStatus.RESTART);
    }

    @DisplayName("플레이어가 다리의 마지막 칸에 도달하면 FINISHED를 반환한다.")
    @Test
    void checkWhetherFinished() {
        IntStream.range(0, 4)
                .forEach(i -> gameStatusOperator.changePosition());
        FinishCondition finishCondition = bridgeGame.checkWhetherFinished();
        assertThat(finishCondition).isEqualTo(FinishCondition.FINISHED);
    }
}