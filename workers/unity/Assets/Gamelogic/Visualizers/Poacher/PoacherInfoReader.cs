using UnityEngine;
using Improbable.Poacher;
using Improbable.Unity.Visualizer;

namespace Assets.Gamelogic.Visualizers.Poacher {
	public class PoacherInfoReader : MonoBehaviour {

		[Require] public PoacherInfoComponentReader PoacherInfoComponentReader;
		public string Name;

		void OnEnable() 
		{
			Name = PoacherInfoComponentReader.Name;
		}
	}
}