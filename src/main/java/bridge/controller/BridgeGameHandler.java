package bridge.controller;

import bridge.core.BridgeGame;
import bridge.type.*;
import bridge.view.InputView;
import bridge.view.OutputView;

import static bridge.view.InputView.readGameCommand;
import static bridge.view.InputView.readSelectedBridgeBlock;

public class BridgeGameHandler {

    public static BridgeGame initGame() {
        return BridgeGame.initBridgeGame(InputView.readBridgeLength());
    }

    public static ProcessCondition executeGame(ProcessCondition processCondition, BridgeGame bridgeGame) {
        if (processCondition.equals(GameStatus.START) || processCondition.equals(PassCondition.PASS)) {
            return executePassCondition(bridgeGame);
        }
        if (processCondition.equals(PassCondition.FAIL)) {
            return executeFailCondition(bridgeGame);
        }
        return null;
    }

    public static ProcessCondition executePassCondition(BridgeGame bridgeGame) {
        ProcessCondition passCondition = bridgeGame.move(readSelectedBridgeBlock());
        OutputView.printMap(passCondition, bridgeGame);
        FinishCondition finishCondition = bridgeGame.checkWhetherFinished(passCondition);
        if (finishCondition.equals(FinishCondition.FINISHED)) return bridgeGame.quit(FinishCondition.FINISHED);
        if (finishCondition.equals(FinishCondition.NOT_FINISHED)) return executeGame(passCondition, bridgeGame);
        return null;
    }

    public static ProcessCondition executeFailCondition(BridgeGame bridgeGame) {
        GameStatus selectGameProcess = GameStatus.getGameStatus(readGameCommand());
        if (selectGameProcess.equals(GameStatus.RESTART)) {
            ProcessCondition processCondition = bridgeGame.retry();
            return executeGame(processCondition, bridgeGame);
        }
        if (selectGameProcess.equals(GameStatus.QUIT)) {
            return bridgeGame.quit(FinishCondition.NOT_FINISHED);
        }
        return null;
    }
}
