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

		void OnEnable()
		{
			PlayerCamera = GetComponentInChildren<Camera>();
            GameObject UiText = GameObject.Find("UiText");
		    if (UiText)
		    {
                UiTextField = UiText.GetComponent<Text>();
            }
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
                        CurrentSelection = newSelection;
                    }
					else
					{
                        CurrentSelection = null;
                    }
				}
			}
		    UpdateSelectionText();
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
