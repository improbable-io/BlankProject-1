using Assets.Gamelogic.Visualizers.Game;
using UnityEngine;
using Improbable.Habitat;
using Improbable.Unity.Visualizer;

namespace Assets.Gamelogic.Visualizers.Habitat {

    public class HabitatInfoReader : MonoBehaviour {

		[Require] private HabitatInfoComponentReader habitatInfoComponentReader;
		public string Name;
		public int Population;
        public GameObject TooltipPrefab;

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

        void Update()
        {
            // this is done here instead of in OnEnable because spatialos will execute stuff in a weird order sometimes
            if (!TooltipPrefab)
            {
                TooltipPrefab = Resources.Load<GameObject>("Models/Tooltip");
                GameObject TooltipInstance = (GameObject)Instantiate(TooltipPrefab, transform.position, Quaternion.identity);
                TooltipInstance.GetComponentInChildren<TextMesh>().text = Name;
            }
        }

        void OnPopulationUpdated(int p)
		{
			Population = p;
            float scale = Mathf.Clamp(Population / 1500f, 20f, 100f);
            transform.localScale = new Vector3 (scale, 1f, scale);
		}
	}
}