using System;
using Assets.Gamelogic.Visualizers.Util;
using UnityEngine;
using Improbable.Poacher;
using Improbable.Unity.Visualizer;
using Life = Improbable.Poacher.Life;

namespace Assets.Gamelogic.Visualizers.Poacher {
	public class PoacherInfoReader : MonoBehaviour {

		[Require] public PoacherInfoComponentReader PoacherInfoComponentReader;
		public string Name;
	    public int Activity;
	    public Life IsAlive;
        public int LastVal;
        public int Ticker = 0;
	    public bool ModelsOn = false;
        public GameObject ModelPrefab;
        public GameObject ModelInstance;

        void OnEnable() 
		{
			Name = PoacherInfoComponentReader.Name;
		    PoacherInfoComponentReader.ActivityUpdated += OnActivityUpdated;
            PoacherInfoComponentReader.LifeUpdated += OnLifeUpdated;
            transform.localScale = new Vector3(10f, 2f, 10f);
            //ModelPrefab = Resources.Load<GameObject>("Models/Knight"); // this is a hack
        }

	    void OnDisable()
	    {
            PoacherInfoComponentReader.ActivityUpdated -= OnActivityUpdated;
            PoacherInfoComponentReader.LifeUpdated -= OnLifeUpdated;
            if (ModelInstance)
            {
                Destroy(ModelInstance);
            }
        }

	    void OnActivityUpdated(int a)
	    {
            Ticker++;
            LastVal = Activity;
            Activity = a;
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
	                ModelInstance = (GameObject) Instantiate(ModelPrefab, transform.position, Quaternion.identity);
	                ModelInstance.AddComponent<HorizontalRotation>();
	            }
	            if (ModelInstance)
	            {
	                var scale = Mathf.Clamp(Activity*0.02f, 5f, 20f);
	                ModelInstance.transform.localScale = Vector3.one * scale;
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
	    }

	    void OnLifeUpdated(Life l)
	    {
	        IsAlive = l;
	        switch (IsAlive)
	        {
	            case Life.Living:
                    break;
                case Life.Dead:
                    GetComponent<Renderer>().material.color = Color.grey;
                    if (ModelInstance)
                    {
                        Destroy(ModelInstance);
                    }
                    break;
                default:
	                break;
	        }
	    }
	}
}
