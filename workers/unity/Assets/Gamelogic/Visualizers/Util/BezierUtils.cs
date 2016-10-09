using UnityEngine;

namespace Assets.Gamelogic.Visualizers.Util
{
    public class MathUtils : MonoBehaviour
    {
        public static Vector3 EvaluteBezier(Vector3 p0, Vector3 p1, Vector3 p2, float t)
        {
            return (1f - t)*((1f - t)*p0 + t*p1) + t*((1 - t)*p1 + t*p2);
        }
    }
}
