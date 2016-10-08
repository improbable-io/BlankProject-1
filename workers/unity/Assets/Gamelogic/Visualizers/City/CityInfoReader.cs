using UnityEngine;
using Improbable.City;
using Improbable.Unity.Visualizer;

namespace Assets.Gamelogic.Visualizers.City {
	public class CityInfoReader : MonoBehaviour {

		[Require] public CityInfoComponentReader CityInfoComponentReader;
		public string Name;
		public float Demand;

		void OnReady() 
		{
			Name = CityInfoComponentReader.Name;
			CityInfoComponentReader.DemandUpdated += OnDemandUpdated;
		}

		void OnDisable()
		{
			CityInfoComponentReader.DemandUpdated -= OnDemandUpdated;
		}

		void OnDemandUpdated(float d)
		{
			Demand = d;
			transform.localScale = new Vector3 (Demand, 1f, Demand);
		}
	}
}