using UnityEngine;
using UnityEngine.UI;

namespace Assets.Gamelogic.Visualizers.Player 
{
	public class EntitySelector : MonoBehaviour 
	{
		public Camera PlayerCamera;
		public GameObject CurrentSelection;
		public Text UiText;

		void OnEnable()
		{
			PlayerCamera = GetComponentInChildren<Camera>();
			UiText = FindObjectOfType(Text);
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
						if (newSelection != CurrentSelection)
						{
							DeselectCurrent();
							CurrentSelection = newSelection;
						}
					}
					else
					{
						DeselectCurrent();
					}
				}
			}
		}

		void SelectEntity(GameObject entity)
		{
			if (!entity.IsEntityObject ()) 
			{
				return;
			}

			if (UiText != null) 
			{
				UiText.text = "Selected: " + entity.EntityId + "Type: " + entity.tag;
			}
		}

		void DeselectCurrent()
		{
			CurrentSelection = null;
		}
	}
}
