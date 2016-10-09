using Assets.Gamelogic.Visualizers.City;
using Assets.Gamelogic.Visualizers.Habitat;
using Assets.Gamelogic.Visualizers.Poacher;
using UnityEngine;
using UnityEngine.UI;

namespace Assets.Gamelogic.Visualizers.Player 
{
	public class EntitySelector : MonoBehaviour 
	{
		public Camera PlayerCamera;
		public GameObject CurrentSelection;
		public Text UiTextField;
	    public GameObject ArrowPrefab;
        public GameObject ArrowInstance;


        void OnEnable()
		{
			PlayerCamera = GetComponentInChildren<Camera>();
            GameObject UiText = GameObject.Find("UiText");
		    if (UiText)
		    {
                UiTextField = UiText.GetComponent<Text>();
            }
            ArrowPrefab = Resources.Load<GameObject>("Models/Arrow");
        }

		void Update () 
		{
			if (Input.GetMouseButtonDown(0))
			{
				RaycastHit hit;
				Ray ray = PlayerCamera.ScreenPointToRay(Input.mousePosition);
				if (Physics.Raycast(ray.origin, ray.direction, out hit))
				{
                    if (hit.collider.gameObject.IsEntityObject())
					{
						GameObject newSelection = hit.collider.gameObject;
					    if (CurrentSelection != newSelection)
					    {
					        DeselectCurrent();
					        SelectCurrent(newSelection);
					    } 
                    }
					else
					{
                        DeselectCurrent();
                    }
				}
			}
		    UpdateSelectionText();
		}

	    void SelectCurrent(GameObject newSelection)
	    {
            CurrentSelection = newSelection;
            ArrowInstance = (GameObject)Instantiate(ArrowPrefab, newSelection.transform.position, Quaternion.Euler(90f, 0f, 0f));
            ArrowInstance.transform.localPosition += new Vector3(0f, 5f, newSelection.transform.localScale.z * 0.5f);
	        ArrowInstance.transform.localScale *= 10f;
            foreach (Transform childTransform in ArrowInstance.transform)
            {
                childTransform.gameObject.GetComponent<Renderer>().material.color = Color.cyan;
            }
        }

	    void DeselectCurrent()
	    {
	        if (ArrowInstance)
	        {
                Destroy(ArrowInstance);
            }
	        CurrentSelection = null;
        }

	    void UpdateSelectionText()
		{
            UiTextField.text = "";
            if (UiTextField && CurrentSelection)
			{
			    switch (CurrentSelection.tag)
			    {
                    case "Habitat":
			            HabitatInfoReader habitatInfoReader = CurrentSelection.GetComponent<HabitatInfoReader>();
			            if (habitatInfoReader)
			            {
                            UiTextField.text += habitatInfoReader.Name + "\nPopulation: " + habitatInfoReader.Population;
                        }
                        break;
                    case "Poacher":
                        PoacherInfoReader poacherInfoReader = CurrentSelection.GetComponent<PoacherInfoReader>();
			            if (poacherInfoReader)
			            {
                            UiTextField.text += poacherInfoReader.Name;
                        }
			            break;
                    case "City":
			            CityInfoReader cityInfoReader = CurrentSelection.GetComponent<CityInfoReader>();
                        if (cityInfoReader)
                        {
                            UiTextField.text += cityInfoReader.name + "\nDemand: " + cityInfoReader.Demand;
                        }
                        break;
                    default:
			            break;
			    }
			}
		}
	}
}
