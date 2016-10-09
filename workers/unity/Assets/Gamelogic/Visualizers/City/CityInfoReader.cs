using System.Collections.Generic;
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
        public float LastVal;
        public int Ticker = 0;
        public IList<ArrowData> Arrows = new List<ArrowData>();
        public GameObject ArrowPrefab;
        public IList<GameObject> ArrowInstances = new List<GameObject>();
	    private IList<GameObject> ArrowTooltips = new List<GameObject>();
        public GameObject ModelPrefab;
	    public GameObject ModelInstance;
	    public GameObject TooltipPrefab;
	    public float Ticktime = 0f;
	    public float TickIntervall = 5f;
        public GameObject ElephantPrefab;

        void OnEnable() 
		{
            ElephantPrefab = Resources.Load<GameObject>("Models/elephant/sav_elephant");
            ArrowPrefab = Resources.Load<GameObject>("Models/BlueCube");
            ModelPrefab = Resources.Load<GameObject>("Models/HexTiles/Tile_City02");
            ModelInstance = (GameObject)Instantiate(ModelPrefab, transform.position, Quaternion.identity);
		    ModelInstance.AddComponent<HorizontalRotation>();
            
            Name = CityInfoComponentReader.Name;
			CityInfoComponentReader.DemandUpdated += OnDemandUpdated;
		    CityInfoComponentReader.ArrowsUpdated += OnArrowsUpdated;
        }

		void OnDisable()
		{
		    if (ModelInstance)
		    {
                Destroy(ModelInstance);
		    }
		    CityInfoComponentReader.DemandUpdated -= OnDemandUpdated;
            CityInfoComponentReader.ArrowsUpdated -= OnArrowsUpdated;
        }

        void Update()
        {
            // this is done here instead of in OnEnable because spatialos will execute stuff in a weird order sometimes
            if (!TooltipPrefab)
            {
                TooltipPrefab = Resources.Load<GameObject>("Models/Tooltip");
            }
            GameObject TooltipInstance = (GameObject)Instantiate(TooltipPrefab, transform.position, Quaternion.identity);
            TooltipInstance.GetComponentInChildren<TextMesh>().text = Name;

            Ticktime += Time.deltaTime;
            if (Ticktime >= TickIntervall)
            {
                Ticktime -= TickIntervall;
                foreach (var item in ArrowInstances)
                {
                    ArrowInfoHolder holder = item.GetComponent<ArrowInfoHolder>();
                    GameObject ElephantTraveller = (GameObject)Instantiate(ElephantPrefab, holder.dest, Quaternion.identity);
                    ElephantTraveller.transform.localScale = Vector3.one * 0.5f * holder.amount;
                    Traveller t = ElephantTraveller.AddComponent<Traveller>();
                    t.destination = transform.position;
                }
            }
        }

        void OnDemandUpdated(int d)
		{
            Ticker++;
            LastVal = Demand;

            Demand = d;
		    float scale = Mathf.Clamp(Demand*2f, 14f, 100f);
			transform.localScale = new Vector3 (scale, 2f, scale);
		    if (ModelInstance)
		    {
                ModelInstance.transform.localScale = Vector3.one * Demand * 0.07f;
            }
		}

	    void OnArrowsUpdated(List<ArrowData> a)
	    {
            if (!TooltipPrefab)
            {
                TooltipPrefab = Resources.Load<GameObject>("Models/Tooltip");
            }
            Arrows = a;
	        foreach (var item in ArrowInstances)
	        {
	            Destroy(item);
	        }
	        foreach (var item in ArrowTooltips)
	        {
	            Destroy(item);
	        }
	        foreach (var item in a)
	        {
                if (item.amount > 0f)
	            {
                    GameObject arrow = (GameObject)Instantiate(ArrowPrefab, transform.position + 0.5f * (item.startingPosition.ToUnityVector() - transform.position), Quaternion.identity);
                    arrow.transform.localScale = new Vector3((transform.position - item.startingPosition.ToUnityVector()).magnitude, 2f, item.amount);
                    arrow.transform.rotation = Quaternion.Euler(0f, GetArrowAngle(transform.position, item.startingPosition.ToUnityVector()), 0f);
	                ArrowInfoHolder holder = arrow.GetComponent<ArrowInfoHolder>();
	                holder.dest = item.startingPosition.ToUnityVector();
	                holder.amount = item.amount;
                    ArrowInstances.Add(arrow);

                    GameObject arrowTooltip = (GameObject)Instantiate(TooltipPrefab, arrow.transform.position, arrow.transform.rotation);
                    arrowTooltip.GetComponentInChildren<TextMesh>().text = "Amount: " + item.amount;
                    ArrowTooltips.Add(arrowTooltip);
                }
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