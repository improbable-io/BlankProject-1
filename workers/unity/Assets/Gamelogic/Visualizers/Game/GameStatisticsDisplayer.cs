using System.Collections.Generic;
using Assets.Gamelogic.Visualizers.Habitat;
using Assets.Gamelogic.Visualizers.Player;
using UnityEngine;
using UnityEngine.UI;

namespace Assets.Gamelogic.Visualizers.Game
{
    public class GameStatisticsDisplayer : MonoBehaviour
    {
        public Text GameText;
        public PlayerInfoReader PlayerInfoReader;
        public IDictionary<string, HabitatInfoReader> PopulationRegistry = new Dictionary<string, HabitatInfoReader>();
        public int AggregatedPopulation;

        void OnEnable()
        {
            GameText = GetComponent<Text>();
        }

        void Update()
        {
            if (GameText != null)
            {
                AggregatePopulation();
                UpdateGameText();
            }
        }

        void AggregatePopulation()
        {
            int result = 0;
            foreach (var item in PopulationRegistry)
            {
                result += item.Value.Population;
            }
            AggregatedPopulation = result;
        }

        void UpdateGameText()
        {
            GameText.text = "";
            if (PlayerInfoReader != null)
            {
                GameText.text += "Funds: £ " + PlayerInfoReader.Money + ((PlayerInfoReader.TempExpenses == 0) ? "" : (" - " + PlayerInfoReader.TempExpenses)) + "\n";
            }
            GameText.text += "Total Elephants: " + AggregatedPopulation;
        }

        public void Subscribe(string habitatName, HabitatInfoReader habitatInfoReader)
        {
            PopulationRegistry.Add(habitatName, habitatInfoReader);
        }

        public void UnSubscribe(string habitatName)
        {
            PopulationRegistry.Remove(habitatName);
        }
    }
}

