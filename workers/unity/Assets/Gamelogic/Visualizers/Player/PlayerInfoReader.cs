using Assets.Gamelogic.Visualizers.Game;
using Improbable.Player;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Visualizers.Player
{
    public class PlayerInfoReader : MonoBehaviour
    {
        [Require] private PlayerInfoComponentReader playerInfoComponentReader;
        public int Money;

        void OnEnable()
        {
            GameObject gameText = GameObject.Find("GameText");
            if (gameText)
            {
                GameStatisticsDisplayer gameStatisticsDisplayer = gameText.GetComponent<GameStatisticsDisplayer>();
                if (gameStatisticsDisplayer)
                {
                    gameStatisticsDisplayer.PlayerInfoReader = this;
                }
            }
            playerInfoComponentReader.MoneyUpdated += OnMoneyUpdated;
        }

        void OnDisable()
        {
            playerInfoComponentReader.MoneyUpdated -= OnMoneyUpdated;
        }

        void OnMoneyUpdated(int m)
        {
            Money = m;
        }
    }
}
