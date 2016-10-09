using UnityEngine;

namespace Assets.Gamelogic.Visualizers.Util
{
    public class Traveller : MonoBehaviour
    {
        public Vector3 destination;
        public float movementSpeed = 60f;

        void Update()
        {
            transform.position = Vector3.MoveTowards(transform.position, destination, movementSpeed * Time.deltaTime);
            transform.rotation = Quaternion.LookRotation(destination - transform.position, Vector3.up);
            if ((transform.position - destination).magnitude <= 2f)
            {
                Destroy(gameObject);
            }
        }
    }
}
