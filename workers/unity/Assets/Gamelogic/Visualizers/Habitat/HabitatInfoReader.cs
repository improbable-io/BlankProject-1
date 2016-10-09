using Assets.Gamelogic.Visualizers.Game;
using UnityEngine;
using Improbable.Habitat;
using Improbable.Unity.Visualizer;

namespace Assets.Gamelogic.Visualizers.Habitat {

    public class HabitatInfoReader : MonoBehaviour {

		[Require] private HabitatInfoComponentReader habitatInfoComponentReader;
		public string Name;
		public int Population;

		void OnEnable()
		{
			Name = habitatInfoComponentReader.Name;
            GameObject gameText = GameObject.Find("GameText");
            if (gameText)
            {
                GameStatisticsDisplayer gameStatisticsDisplayer = gameText.GetComponent<GameStatisticsDisplayer>();
                if (gameStatisticsDisplayer)
                {
                    gameStatisticsDisplayer.Subscribe(this.Name, this);
                }
            }
            habitatInfoComponentReader.PopulationUpdated += OnPopulationUpdated;
		}

		void OnDisable()
		{
			habitatInfoComponentReader.PopulationUpdated -= OnPopulationUpdated;
		}

		void OnPopulationUpdated(int p)
		{
			Population = p;
			transform.localScale = new Vector3 (Population / 1000f, 1f, Population / 1000f);
		}
	}
}