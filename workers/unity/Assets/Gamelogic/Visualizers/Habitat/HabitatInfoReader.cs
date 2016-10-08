using UnityEngine;
using Improbable.Habitat;
using Improbable.Unity.Visualizer;

namespace Assets.Gamelogic.Visualizers.Habitat {
	public class HabitatInfoReader : MonoBehaviour {

		[Require] public HabitatInfoComponentReader HabitatInfoComponentReader;
		public string Name;
		public int Population;

		void OnReady() 
		{
			Name = HabitatInfoComponentReader.Name;
			HabitatInfoComponentReader.PopulationUpdated += OnPopulationUpdated;
		}

		void OnDisable()
		{
			HabitatInfoComponentReader.PopulationUpdated -= OnPopulationUpdated;
		}

		void OnPopulationUpdated(int p)
		{
			Population = p;
			transform.localScale = new Vector3 (Population / 1000f, 1f, Population / 1000f);
		}
	}
}