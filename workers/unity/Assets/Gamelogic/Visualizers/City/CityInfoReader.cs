using Assets.Gamelogic.Visualizers.Util;
using UnityEngine;
using Improbable.City;
using Improbable.Math;
using Improbable.Unity.Common.Core.Math;
using Improbable.Unity.Visualizer;

namespace Assets.Gamelogic.Visualizers.City
{
	public class CityInfoReader : MonoBehaviour {

		[Require] public CityInfoComponentReader CityInfoComponentReader;
		public string Name;
		public float Demand;
	    public ArrowData ArrowData = new ArrowData(Vector3d.ZERO, 0f);
        public GameObject ArrowPrefab;
        public GameObject ArrowInstance;
	    private GameObject ArrowTooltip;
        public GameObject ModelPrefab;
	    public GameObject ModelInstance;
	    public GameObject TooltipPrefab;

		void OnEnable() 
		{
            ArrowPrefab = Resources.Load<GameObject>("Models/BlueCube");
            ModelPrefab = Resources.Load<GameObject>("Models/HexTiles/Tile_City02");
            ModelInstance = (GameObject)Instantiate(ModelPrefab, transform.position, Quaternion.identity);
		    ModelInstance.AddComponent<HorizontalRotation>();
            
            Name = CityInfoComponentReader.Name;
			CityInfoComponentReader.DemandUpdated += OnDemandUpdated;
		    CityInfoComponentReader.ArrowDataUpdated += OnArrowDataUpdated;
		}

		void OnDisable()
		{
		    if (ModelInstance)
		    {
                Destroy(ModelInstance);
		    }
		    CityInfoComponentReader.DemandUpdated -= OnDemandUpdated;
            CityInfoComponentReader.ArrowDataUpdated -= OnArrowDataUpdated;
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

	    void OnArrowDataUpdated(ArrowData a)
	    {
            if (ArrowInstance)
            {
                Destroy(ArrowInstance);
                Destroy(ArrowTooltip);
            }
            if (a.Amount > 0f)
            {
                Debug.Log("hoho" + Name + " to " + a.startingPosition.ToUnityVector());
	            ArrowInstance = (GameObject)Instantiate(ArrowPrefab, transform.position + 0.5f*(a.startingPosition.ToUnityVector() - transform.position), Quaternion.identity);
	            ArrowInstance.transform.localScale = new Vector3((transform.position - a.startingPosition.ToUnityVector()).magnitude, 2f, a.amount);
	            ArrowInstance.transform.rotation = Quaternion.Euler(0f, GetArrowAngle(transform.position, a.startingPosition.ToUnityVector()), 0f);

                if (!TooltipPrefab)
                {
                    TooltipPrefab = Resources.Load<GameObject>("Models/Tooltip");
                }
                ArrowTooltip = (GameObject)Instantiate(TooltipPrefab, ArrowInstance.transform.position, ArrowInstance.transform.rotation);
                ArrowTooltip.GetComponentInChildren<TextMesh>().text = "Amount: " + a.amount;
            }
	    }

	    float GetArrowAngle(Vector3 a, Vector3 b)
	    {
	        float dy = Mathf.Abs(b.z - a.z);
            float dx = Mathf.Abs(b.x - a.x);
	        return Mathf.Atan(Mathf.Tan(dy / dx)) * 180f / Mathf.PI * -1f;
	    }
	}
}   