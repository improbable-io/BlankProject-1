﻿using Assets.Gamelogic.Visualizers.Game;
using Assets.Gamelogic.Visualizers.Util;
using UnityEngine;
using Improbable.Habitat;
using Improbable.Unity.Visualizer;

namespace Assets.Gamelogic.Visualizers.Habitat {

    public class HabitatInfoReader : MonoBehaviour {

		[Require] private HabitatInfoComponentReader habitatInfoComponentReader;
		public string Name;
		public int Population;
        public int LastVal;
        public int Ticker = 0;
        public GameObject TooltipPrefab;
        public bool ModelsOn = true;
        public GameObject ModelPrefab;
        public GameObject ModelInstance;

        void OnEnable()
		{
            ModelPrefab = Resources.Load<GameObject>("Models/elephant/sav_elephant");
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

        void ToggleModels()
        {
            ModelsOn = !ModelsOn;
            if (!ModelsOn && ModelInstance)
            {
                Destroy(ModelInstance);
            }
        }

        void DrawModel()
        {
            if (ModelsOn)
            {
                // this is done here instead of in OnActivityUpdated because spatialos will execute stuff in a weird order sometimes
                if (!ModelInstance)
                {
                    ModelInstance = (GameObject)Instantiate(ModelPrefab, transform.position, Quaternion.identity);
                    ModelInstance.AddComponent<HorizontalRotation>();
                }
                if (ModelInstance)
                {
                    ModelInstance.transform.localScale = Vector3.one * Population * 0.0001f;
                }
            }
        }

        void Update()
        {
            if (Input.GetKeyDown(KeyCode.T))
            {
                ToggleModels();
            }
            DrawModel();

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
            LastVal = Population;
            Ticker++;

			Population = p;
            float scale = Mathf.Clamp(Population / 1500f, 20f, 100f);
            transform.localScale = new Vector3 (scale, 1f, scale);
            float t = Mathf.Clamp(Population/120000f, 0f, 1f);
            Vector3 clr = Vector3.Lerp(new Vector3(0f, 140f/255f, 4f / 255f), new Vector3(0f, 1f, 80f/255f), t);
            GetComponent<Renderer>().material.color = new Color(clr.x, clr.y, clr.z);
        }
    }
}