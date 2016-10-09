﻿using Assets.Gamelogic.Visualizers.Util;
using UnityEngine;
using Improbable.City;
using Improbable.Unity.Visualizer;

namespace Assets.Gamelogic.Visualizers.City
{
	public class CityInfoReader : MonoBehaviour {

		[Require] public CityInfoComponentReader CityInfoComponentReader;
		public string Name;
		public float Demand;
	    public GameObject ModelPrefab;
	    public GameObject ModelInstance;
	    public GameObject TooltipPrefab;

		void OnEnable() 
		{
            ModelPrefab = Resources.Load<GameObject>("Models/HexTiles/Tile_City02");
            ModelInstance = (GameObject)Instantiate(ModelPrefab, transform.position, Quaternion.identity);
		    ModelInstance.AddComponent<HorizontalRotation>();
            
            Name = CityInfoComponentReader.Name;
			CityInfoComponentReader.DemandUpdated += OnDemandUpdated;
		}

		void OnDisable()
		{
		    if (ModelInstance)
		    {
                Destroy(ModelInstance);
		    }
		    CityInfoComponentReader.DemandUpdated -= OnDemandUpdated;
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

        void OnDemandUpdated(int d)
		{
			Demand = d;
		    float scale = Mathf.Clamp(Demand*2f, 14f, 100f);
			transform.localScale = new Vector3 (scale, 2f, scale);
		    if (ModelInstance)
		    {
                ModelInstance.transform.localScale = Vector3.one * Demand * 0.07f;
            }
		}
	}
}   