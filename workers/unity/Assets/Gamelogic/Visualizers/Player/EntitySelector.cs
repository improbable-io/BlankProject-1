using Assets.Gamelogic.Visualizers.City;
using Assets.Gamelogic.Visualizers.Habitat;
using Assets.Gamelogic.Visualizers.Poacher;
using Assets.Gamelogic.Visualizers.Util;
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
				}
			}
		    if (Input.GetMouseButtonDown(1))
		    {
                DeselectCurrent();
            }

		    UpdateSelectionText();
		}

	    void SelectCurrent(GameObject newSelection)
	    {
            CurrentSelection = newSelection;
            ArrowInstance = (GameObject)Instantiate(ArrowPrefab, newSelection.transform.position, Quaternion.identity);
            ArrowInstance.transform.localScale *= 20f;
	        ArrowInstance.AddComponent<HorizontalRotation>();
            foreach (Transform childTransform in ArrowInstance.transform)
            {
                childTransform.gameObject.GetComponent<Renderer>().material.color = new Color(40f/255f, 140f/255f, 1f);
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
			                UiTextField.text += habitatInfoReader.Name + "\n";
                            UiTextField.text += "Population: " + habitatInfoReader.Population + "\n";
                            UiTextField.text += (habitatInfoReader.Ticker >= 2) ? "Change: " + (habitatInfoReader.Population - habitatInfoReader.LastVal) + "\n" : "\n";
                            UiTextField.text += "\n";
                            UiTextField.text += "Invest in randers here: \n";
                        }
                        break;
                    case "Poacher":
                        PoacherInfoReader poacherInfoReader = CurrentSelection.GetComponent<PoacherInfoReader>();
			            if (poacherInfoReader)
			            {
                            UiTextField.text += poacherInfoReader.Name + "\n";
                            UiTextField.text += "Activity: " + poacherInfoReader.Activity + "\n";
                            UiTextField.text += (poacherInfoReader.Ticker >= 2) ? "Change: " + (poacherInfoReader.Activity - poacherInfoReader.LastVal) + "\n" : "\n";
                            UiTextField.text += "\n";
                            UiTextField.text += "Invest in law enforcement here: \n";
                        }
			            break;
                    case "City":
			            CityInfoReader cityInfoReader = CurrentSelection.GetComponent<CityInfoReader>();
                        if (cityInfoReader)
                        {
                            UiTextField.text += cityInfoReader.Name + "\n";
                            UiTextField.text += "Demand: " + cityInfoReader.Demand + "\n";
                            UiTextField.text += (cityInfoReader.Ticker >= 2) ? "Change: " + (cityInfoReader.Demand - cityInfoReader.LastVal) + "\n" : "\n";
                            UiTextField.text += "\n";
                            UiTextField.text += "Invest in PR campaigns here: \n";
                        }
                        break;
                    default:
			            break;
			    }
			}
		}
	}
}
