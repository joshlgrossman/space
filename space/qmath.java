package space;

public class qmath {
	public static final float[] SINE = {0.0f, 0.01f, 0.02f, 0.03f, 0.04f, 0.05f, 0.06f, 0.0699f, 0.0799f, 0.0899f, 0.0998f, 0.1098f, 0.1197f, 0.1296f, 0.1395f, 0.1494f, 0.1593f, 0.1692f, 0.179f, 0.1889f, 0.1987f, 0.2085f, 0.2182f, 0.228f, 0.2377f, 0.2474f, 0.2571f, 0.2667f, 0.2764f, 0.286f, 0.2955f, 0.3051f, 0.3146f, 0.324f, 0.3335f, 0.3429f, 0.3523f, 0.3616f, 0.3709f, 0.3802f, 0.3894f, 0.3986f, 0.4078f, 0.4169f, 0.4259f, 0.435f, 0.4439f, 0.4529f, 0.4618f, 0.4706f, 0.4794f, 0.4882f, 0.4969f, 0.5055f, 0.5141f, 0.5227f, 0.5312f, 0.5396f, 0.548f, 0.5564f, 0.5646f, 0.5729f, 0.581f, 0.5891f, 0.5972f, 0.6052f, 0.6131f, 0.621f, 0.6288f, 0.6365f, 0.6442f, 0.6518f, 0.6594f, 0.6669f, 0.6743f, 0.6816f, 0.6889f, 0.6961f, 0.7033f, 0.7104f, 0.7174f, 0.7243f, 0.7311f, 0.7379f, 0.7446f, 0.7513f, 0.7578f, 0.7643f, 0.7707f, 0.7771f, 0.7833f, 0.7895f, 0.7956f, 0.8016f, 0.8076f, 0.8134f, 0.8192f, 0.8249f, 0.8305f, 0.836f, 0.8415f, 0.8468f, 0.8521f, 0.8573f, 0.8624f, 0.8674f, 0.8724f, 0.8772f, 0.882f, 0.8866f, 0.8912f, 0.8957f, 0.9001f, 0.9044f, 0.9086f, 0.9128f, 0.9168f, 0.9208f, 0.9246f, 0.9284f, 0.932f, 0.9356f, 0.9391f, 0.9425f, 0.9458f, 0.949f, 0.9521f, 0.9551f, 0.958f, 0.9608f, 0.9636f, 0.9662f, 0.9687f, 0.9711f, 0.9735f, 0.9757f, 0.9779f, 0.9799f, 0.9819f, 0.9837f, 0.9854f, 0.9871f, 0.9887f, 0.9901f, 0.9915f, 0.9927f, 0.9939f, 0.9949f, 0.9959f, 0.9967f, 0.9975f, 0.9982f, 0.9987f, 0.9992f, 0.9995f, 0.9998f, 0.9999f, 1.0f, 1.0f, 0.9998f, 0.9996f, 0.9992f, 0.9988f, 0.9982f, 0.9976f, 0.9969f, 0.996f, 0.9951f, 0.994f, 0.9929f, 0.9917f, 0.9903f, 0.9889f, 0.9874f, 0.9857f, 0.984f, 0.9822f, 0.9802f, 0.9782f, 0.9761f, 0.9738f, 0.9715f, 0.9691f, 0.9666f, 0.964f, 0.9613f, 0.9585f, 0.9556f, 0.9526f, 0.9495f, 0.9463f, 0.943f, 0.9396f, 0.9362f, 0.9326f, 0.929f, 0.9252f, 0.9214f, 0.9174f, 0.9134f, 0.9093f, 0.9051f, 0.9008f, 0.8964f, 0.8919f, 0.8874f, 0.8827f, 0.878f, 0.8731f, 0.8682f, 0.8632f, 0.8581f, 0.8529f, 0.8477f, 0.8423f, 0.8369f, 0.8314f, 0.8258f, 0.8201f, 0.8143f, 0.8085f, 0.8026f, 0.7966f, 0.7905f, 0.7843f, 0.7781f, 0.7718f, 0.7654f, 0.7589f, 0.7523f, 0.7457f, 0.739f, 0.7322f, 0.7254f, 0.7185f, 0.7115f, 0.7044f, 0.6973f, 0.6901f, 0.6828f, 0.6755f, 0.6681f, 0.6606f, 0.653f, 0.6454f, 0.6378f, 0.63f, 0.6222f, 0.6144f, 0.6065f, 0.5985f, 0.5904f, 0.5823f, 0.5742f, 0.566f, 0.5577f, 0.5494f, 0.541f, 0.5325f, 0.524f, 0.5155f, 0.5069f, 0.4983f, 0.4896f, 0.4808f, 0.472f, 0.4632f, 0.4543f, 0.4454f, 0.4364f, 0.4274f, 0.4183f, 0.4092f, 0.4001f, 0.3909f, 0.3817f, 0.3724f, 0.3631f, 0.3538f, 0.3444f, 0.335f, 0.3255f, 0.3161f, 0.3066f, 0.297f, 0.2875f, 0.2779f, 0.2683f, 0.2586f, 0.2489f, 0.2392f, 0.2295f, 0.2198f, 0.21f, 0.2002f, 0.1904f, 0.1806f, 0.1708f, 0.1609f, 0.151f, 0.1411f, 0.1312f, 0.1213f, 0.1114f, 0.1014f, 0.0915f, 0.0815f, 0.0715f, 0.0616f, 0.0516f, 0.0416f, 0.0316f, 0.0216f, 0.0116f, 0.0016f, -0.0084f, -0.0184f, -0.0284f, -0.0384f, -0.0484f, -0.0584f, -0.0684f, -0.0783f, -0.0883f, -0.0982f, -0.1082f, -0.1181f, -0.1281f, -0.138f, -0.1479f, -0.1577f, -0.1676f, -0.1775f, -0.1873f, -0.1971f, -0.2069f, -0.2167f, -0.2264f, -0.2362f, -0.2459f, -0.2555f, -0.2652f, -0.2748f, -0.2844f, -0.294f, -0.3035f, -0.3131f, -0.3225f, -0.332f, -0.3414f, -0.3508f, -0.3601f, -0.3694f, -0.3787f, -0.388f, -0.3971f, -0.4063f, -0.4154f, -0.4245f, -0.4335f, -0.4425f, -0.4515f, -0.4604f, -0.4692f, -0.478f, -0.4868f, -0.4955f, -0.5042f, -0.5128f, -0.5213f, -0.5298f, -0.5383f, -0.5467f, -0.555f, -0.5633f, -0.5716f, -0.5797f, -0.5879f, -0.5959f, -0.6039f, -0.6119f, -0.6197f, -0.6276f, -0.6353f, -0.643f, -0.6506f, -0.6582f, -0.6657f, -0.6731f, -0.6805f, -0.6878f, -0.695f, -0.7021f, -0.7092f, -0.7162f, -0.7232f, -0.7301f, -0.7369f, -0.7436f, -0.7502f, -0.7568f, -0.7633f, -0.7697f, -0.7761f, -0.7823f, -0.7885f, -0.7946f, -0.8007f, -0.8066f, -0.8125f, -0.8183f, -0.824f, -0.8296f, -0.8352f, -0.8406f, -0.846f, -0.8513f, -0.8565f, -0.8616f, -0.8666f, -0.8716f, -0.8764f, -0.8812f, -0.8859f, -0.8905f, -0.895f, -0.8994f, -0.9037f, -0.908f, -0.9121f, -0.9162f, -0.9201f, -0.924f, -0.9278f, -0.9315f, -0.9351f, -0.9386f, -0.942f, -0.9453f, -0.9485f, -0.9516f, -0.9546f, -0.9576f, -0.9604f, -0.9631f, -0.9658f, -0.9683f, -0.9708f, -0.9731f, -0.9754f, -0.9775f, -0.9796f, -0.9816f, -0.9834f, -0.9852f, -0.9868f, -0.9884f, -0.9899f, -0.9912f, -0.9925f, -0.9937f, -0.9948f, -0.9957f, -0.9966f, -0.9974f, -0.9981f, -0.9986f, -0.9991f, -0.9995f, -0.9997f, -0.9999f, -1.0f, -1.0f, -0.9998f, -0.9996f, -0.9993f, -0.9989f, -0.9983f, -0.9977f, -0.997f, -0.9962f, -0.9952f, -0.9942f, -0.9931f, -0.9919f, -0.9905f, -0.9891f, -0.9876f, -0.986f, -0.9843f, -0.9825f, -0.9805f, -0.9785f, -0.9764f, -0.9742f, -0.9719f, -0.9695f, -0.967f, -0.9644f, -0.9617f, -0.9589f, -0.956f, -0.9531f, -0.95f, -0.9468f, -0.9435f, -0.9402f, -0.9367f, -0.9332f, -0.9295f, -0.9258f, -0.922f, -0.9181f, -0.9141f, -0.91f, -0.9058f, -0.9015f, -0.8971f, -0.8926f, -0.8881f, -0.8835f, -0.8787f, -0.8739f, -0.869f, -0.864f, -0.8589f, -0.8538f, -0.8485f, -0.8432f, -0.8378f, -0.8323f, -0.8267f, -0.821f, -0.8153f, -0.8094f, -0.8035f, -0.7975f, -0.7915f, -0.7853f, -0.7791f, -0.7728f, -0.7664f, -0.7599f, -0.7534f, -0.7468f, -0.7401f, -0.7333f, -0.7265f, -0.7196f, -0.7126f, -0.7055f, -0.6984f, -0.6912f, -0.684f, -0.6766f, -0.6692f, -0.6618f, -0.6542f, -0.6467f, -0.639f, -0.6313f, -0.6235f, -0.6156f, -0.6077f, -0.5997f, -0.5917f, -0.5836f, -0.5755f, -0.5673f, -0.559f, -0.5507f, -0.5423f, -0.5339f, -0.5254f, -0.5169f, -0.5083f, -0.4996f, -0.491f, -0.4822f, -0.4734f, -0.4646f, -0.4557f, -0.4468f, -0.4378f, -0.4288f, -0.4198f, -0.4107f, -0.4015f, -0.3924f, -0.3831f, -0.3739f, -0.3646f, -0.3553f, -0.3459f, -0.3365f, -0.3271f, -0.3176f, -0.3081f, -0.2986f, -0.289f, -0.2794f, -0.2698f, -0.2602f, -0.2505f, -0.2408f, -0.2311f, -0.2213f, -0.2116f, -0.2018f, -0.192f, -0.1822f, -0.1723f, -0.1625f, -0.1526f, -0.1427f, -0.1328f, -0.1229f, -0.1129f, -0.103f, -0.0931f, -0.0831f, -0.0731f, -0.0631f, -0.0532f, -0.0432f, -0.0332f, -0.0232f, -0.0132f, 0f};
	public static final float[] COSINE = {1.0f, 1.0f, 0.9998f, 0.9996f, 0.9992f, 0.9988f, 0.9982f, 0.9976f, 0.9968f, 0.996f, 0.995f, 0.994f, 0.9928f, 0.9916f, 0.9902f, 0.9888f, 0.9872f, 0.9856f, 0.9838f, 0.982f, 0.9801f, 0.978f, 0.9759f, 0.9737f, 0.9713f, 0.9689f, 0.9664f, 0.9638f, 0.9611f, 0.9582f, 0.9553f, 0.9523f, 0.9492f, 0.946f, 0.9428f, 0.9394f, 0.9359f, 0.9323f, 0.9287f, 0.9249f, 0.9211f, 0.9171f, 0.9131f, 0.909f, 0.9048f, 0.9004f, 0.8961f, 0.8916f, 0.887f, 0.8823f, 0.8776f, 0.8727f, 0.8678f, 0.8628f, 0.8577f, 0.8525f, 0.8473f, 0.8419f, 0.8365f, 0.8309f, 0.8253f, 0.8196f, 0.8139f, 0.808f, 0.8021f, 0.7961f, 0.79f, 0.7838f, 0.7776f, 0.7712f, 0.7648f, 0.7584f, 0.7518f, 0.7452f, 0.7385f, 0.7317f, 0.7248f, 0.7179f, 0.7109f, 0.7038f, 0.6967f, 0.6895f, 0.6822f, 0.6749f, 0.6675f, 0.66f, 0.6524f, 0.6448f, 0.6372f, 0.6294f, 0.6216f, 0.6137f, 0.6058f, 0.5978f, 0.5898f, 0.5817f, 0.5735f, 0.5653f, 0.557f, 0.5487f, 0.5403f, 0.5319f, 0.5234f, 0.5148f, 0.5062f, 0.4976f, 0.4889f, 0.4801f, 0.4713f, 0.4625f, 0.4536f, 0.4447f, 0.4357f, 0.4267f, 0.4176f, 0.4085f, 0.3993f, 0.3902f, 0.3809f, 0.3717f, 0.3624f, 0.353f, 0.3436f, 0.3342f, 0.3248f, 0.3153f, 0.3058f, 0.2963f, 0.2867f, 0.2771f, 0.2675f, 0.2579f, 0.2482f, 0.2385f, 0.2288f, 0.219f, 0.2092f, 0.1994f, 0.1896f, 0.1798f, 0.17f, 0.1601f, 0.1502f, 0.1403f, 0.1304f, 0.1205f, 0.1106f, 0.1006f, 0.0907f, 0.0807f, 0.0707f, 0.0608f, 0.0508f, 0.0408f, 0.0308f, 0.0208f, 0.0108f, 8.0E-4f, -0.0092f, -0.0192f, -0.0292f, -0.0392f, -0.0492f, -0.0592f, -0.0691f, -0.0791f, -0.0891f, -0.099f, -0.109f, -0.1189f, -0.1288f, -0.1388f, -0.1487f, -0.1585f, -0.1684f, -0.1782f, -0.1881f, -0.1979f, -0.2077f, -0.2175f, -0.2272f, -0.2369f, -0.2466f, -0.2563f, -0.266f, -0.2756f, -0.2852f, -0.2948f, -0.3043f, -0.3138f, -0.3233f, -0.3327f, -0.3421f, -0.3515f, -0.3609f, -0.3702f, -0.3795f, -0.3887f, -0.3979f, -0.407f, -0.4161f, -0.4252f, -0.4342f, -0.4432f, -0.4522f, -0.4611f, -0.4699f, -0.4787f, -0.4875f, -0.4962f, -0.5048f, -0.5135f, -0.522f, -0.5305f, -0.539f, -0.5474f, -0.5557f, -0.564f, -0.5722f, -0.5804f, -0.5885f, -0.5966f, -0.6046f, -0.6125f, -0.6204f, -0.6282f, -0.6359f, -0.6436f, -0.6512f, -0.6588f, -0.6663f, -0.6737f, -0.6811f, -0.6883f, -0.6956f, -0.7027f, -0.7098f, -0.7168f, -0.7237f, -0.7306f, -0.7374f, -0.7441f, -0.7508f, -0.7573f, -0.7638f, -0.7702f, -0.7766f, -0.7828f, -0.789f, -0.7951f, -0.8011f, -0.8071f, -0.813f, -0.8187f, -0.8244f, -0.8301f, -0.8356f, -0.841f, -0.8464f, -0.8517f, -0.8569f, -0.862f, -0.867f, -0.872f, -0.8768f, -0.8816f, -0.8863f, -0.8908f, -0.8953f, -0.8998f, -0.9041f, -0.9083f, -0.9124f, -0.9165f, -0.9204f, -0.9243f, -0.9281f, -0.9318f, -0.9353f, -0.9388f, -0.9422f, -0.9455f, -0.9487f, -0.9518f, -0.9549f, -0.9578f, -0.9606f, -0.9633f, -0.966f, -0.9685f, -0.971f, -0.9733f, -0.9755f, -0.9777f, -0.9797f, -0.9817f, -0.9836f, -0.9853f, -0.987f, -0.9885f, -0.99f, -0.9914f, -0.9926f, -0.9938f, -0.9948f, -0.9958f, -0.9967f, -0.9974f, -0.9981f, -0.9987f, -0.9991f, -0.9995f, -0.9998f, -0.9999f, -1.0f, -1.0f, -0.9998f, -0.9996f, -0.9993f, -0.9988f, -0.9983f, -0.9977f, -0.9969f, -0.9961f, -0.9952f, -0.9941f, -0.993f, -0.9918f, -0.9904f, -0.989f, -0.9875f, -0.9859f, -0.9841f, -0.9823f, -0.9804f, -0.9784f, -0.9762f, -0.974f, -0.9717f, -0.9693f, -0.9668f, -0.9642f, -0.9615f, -0.9587f, -0.9558f, -0.9528f, -0.9497f, -0.9466f, -0.9433f, -0.9399f, -0.9365f, -0.9329f, -0.9293f, -0.9255f, -0.9217f, -0.9178f, -0.9137f, -0.9096f, -0.9054f, -0.9011f, -0.8968f, -0.8923f, -0.8877f, -0.8831f, -0.8783f, -0.8735f, -0.8686f, -0.8636f, -0.8585f, -0.8534f, -0.8481f, -0.8428f, -0.8373f, -0.8318f, -0.8262f, -0.8206f, -0.8148f, -0.809f, -0.803f, -0.797f, -0.791f, -0.7848f, -0.7786f, -0.7723f, -0.7659f, -0.7594f, -0.7529f, -0.7462f, -0.7395f, -0.7328f, -0.7259f, -0.719f, -0.712f, -0.705f, -0.6978f, -0.6907f, -0.6834f, -0.6761f, -0.6686f, -0.6612f, -0.6536f, -0.646f, -0.6384f, -0.6306f, -0.6229f, -0.615f, -0.6071f, -0.5991f, -0.5911f, -0.583f, -0.5748f, -0.5666f, -0.5583f, -0.55f, -0.5416f, -0.5332f, -0.5247f, -0.5162f, -0.5076f, -0.499f, -0.4903f, -0.4815f, -0.4727f, -0.4639f, -0.455f, -0.4461f, -0.4371f, -0.4281f, -0.419f, -0.4099f, -0.4008f, -0.3916f, -0.3824f, -0.3731f, -0.3638f, -0.3545f, -0.3451f, -0.3357f, -0.3263f, -0.3168f, -0.3073f, -0.2978f, -0.2882f, -0.2787f, -0.269f, -0.2594f, -0.2497f, -0.24f, -0.2303f, -0.2206f, -0.2108f, -0.201f, -0.1912f, -0.1814f, -0.1715f, -0.1617f, -0.1518f, -0.1419f, -0.132f, -0.1221f, -0.1122f, -0.1022f, -0.0923f, -0.0823f, -0.0723f, -0.0623f, -0.0524f, -0.0424f, -0.0324f, -0.0224f, -0.0124f, -0.0024f, 0.0076f, 0.0176f, 0.0276f, 0.0376f, 0.0476f, 0.0576f, 0.0676f, 0.0775f, 0.0875f, 0.0975f, 0.1074f, 0.1173f, 0.1273f, 0.1372f, 0.1471f, 0.157f, 0.1668f, 0.1767f, 0.1865f, 0.1963f, 0.2061f, 0.2159f, 0.2257f, 0.2354f, 0.2451f, 0.2548f, 0.2644f, 0.2741f, 0.2837f, 0.2932f, 0.3028f, 0.3123f, 0.3218f, 0.3312f, 0.3407f, 0.35f, 0.3594f, 0.3687f, 0.378f, 0.3872f, 0.3964f, 0.4056f, 0.4147f, 0.4238f, 0.4328f, 0.4418f, 0.4508f, 0.4597f, 0.4685f, 0.4773f, 0.4861f, 0.4948f, 0.5035f, 0.5121f, 0.5206f, 0.5292f, 0.5376f, 0.546f, 0.5544f, 0.5627f, 0.5709f, 0.5791f, 0.5872f, 0.5953f, 0.6033f, 0.6112f, 0.6191f, 0.6269f, 0.6347f, 0.6424f, 0.65f, 0.6576f, 0.6651f, 0.6725f, 0.6799f, 0.6872f, 0.6944f, 0.7016f, 0.7087f, 0.7157f, 0.7226f, 0.7295f, 0.7363f, 0.743f, 0.7497f, 0.7563f, 0.7628f, 0.7692f, 0.7756f, 0.7818f, 0.788f, 0.7942f, 0.8002f, 0.8061f, 0.812f, 0.8178f, 0.8235f, 0.8292f, 0.8347f, 0.8402f, 0.8456f, 0.8509f, 0.8561f, 0.8612f, 0.8662f, 0.8712f, 0.8761f, 0.8808f, 0.8855f, 0.8901f, 0.8946f, 0.8991f, 0.9034f, 0.9076f, 0.9118f, 0.9158f, 0.9198f, 0.9237f, 0.9275f, 0.9312f, 0.9348f, 0.9383f, 0.9417f, 0.945f, 0.9482f, 0.9514f, 0.9544f, 0.9573f, 0.9602f, 0.9629f, 0.9656f, 0.9681f, 0.9706f, 0.9729f, 0.9752f, 0.9774f, 0.9794f, 0.9814f, 0.9833f, 0.985f, 0.9867f, 0.9883f, 0.9898f, 0.9911f, 0.9924f, 0.9936f, 0.9947f, 0.9957f, 0.9965f, 0.9973f, 0.998f, 0.9986f, 0.9991f, 0.9994f, 0.9997f, 0.9999f, 1f};
	public static final float TAU = 6.28318530718f;
	
	public static final float[] RANDOM = {0.1912f,0.176f,0.0907f,0.6313f,0.6045f,0.046f,0.6947f,0.5847f,0.2287f,0.0831f,0.4799f,0.8951f,0.785f,0.9001f,0.5817f,0.3123f,0.2341f,0.0145f,0.9631f,0.5476f,0.924f,0.8963f,0.5157f,0.096f,0.4955f,0.999f,0.4322f,0.343f,0.6335f,0.3847f,0.4695f,0.6505f,0.1992f,0.9159f,0.0618f,0.9894f,0.7551f,0.6249f,0.1064f,0.3483f,0.0037f,0.7989f,0.6482f,0.5816f,0.2018f,0.5194f,0.6186f,0.5831f,0.1821f,0.8822f,0.2447f,0.6704f,0.2163f,0.8893f,0.7402f,0.8494f,0.5902f,0.0527f,0.4123f,0.1891f,0.4229f,0.0794f,0.3756f,0.8852f,0.9608f,0.616f,0.6494f,0.7124f,0.3038f,0.9252f,0.5352f,0.4771f,0.2802f,0.5365f,0.8183f,0.7681f,0.9767f,0.3695f,0.5198f,0.7724f,0.2504f,0.5636f,0.6357f,0.1759f,0.4046f,0.2623f,0.5452f,0.1995f,0.0903f,0.9019f,0.5414f,0.6352f,0.4147f,0.1292f,0.2475f,0.3415f,0.1803f,0.3368f,0.5136f,0.0339f,0.1575f,0.944f,0.7622f,0.3616f,0.3044f,0.9753f,0.6463f,0.2027f,0.8825f,0.191f,0.3166f,0.8282f,0.4934f,0.0129f,0.5554f,0.0856f,0.1617f,0.7504f,0.5285f,0.0375f,0.7226f,0.1849f,0.9298f,0.5506f,0.939f,0.3942f,0.8513f,0.1799f,0.9693f,0.8147f,0.7941f,0.3125f,0.033f,0.9259f,0.8586f,0.9501f,0.3822f,0.6172f,0.2989f,0.6908f,0.9747f,0.2508f,0.307f,0.4461f,0.5685f,0.6687f,0.1059f,0.1753f,0.3072f,0.9103f,0.1721f,0.4567f,0.2513f,0.2027f,0.6836f,0.0223f,0.7263f,0.8669f,0.0953f,0.4844f,0.801f,0.1423f,0.0792f,0.3528f,0.6922f,0.574f,0.9361f,0.1009f,0.396f,0.5295f,0.9856f,0.478f,0.3896f,0.6168f,0.3691f,0.4837f,0.8157f,0.2602f,0.167f,0.4214f,0.1668f,0.4573f,0.7181f,0.231f,0.119f,0.7871f,0.5614f,0.8631f,0.8304f,0.0717f,0.4683f,0.8028f,0.7142f,0.6992f,0.6261f,0.9769f,0.4276f,0.5126f,0.1487f,0.1851f,0.5717f,0.3381f,0.7412f,0.8843f,0.3919f,0.6822f,0.5248f,0.6263f,0.5861f,0.8625f,0.7405f,0.697f,0.1835f,0.8522f,0.2947f,0.5419f,0.3387f,0.4191f,0.8823f,0.3711f,0.8013f,0.7145f,0.7285f,0.9364f,0.7673f,0.3324f,0.8303f,0.8457f,0.7976f,0.9985f,0.9428f,0.9389f,0.4948f,0.4709f,0.4581f,0.188f,0.6725f,0.9404f,0.7529f,0.3014f,0.8214f,0.2023f,0.5455f,0.7185f,0.7775f,0.1386f,0.2922f,0.9561f,0.635f,0.5967f,0.6822f,0.3382f,0.1079f,0.5817f,0.8414f,0.9804f};
	public static final int[] RANDOM_INT = { 53, 249, 30, 97, 197, 86, 236, 191, 82, 27, 116, 125, 182, 206, 255, 234, 56, 69, 129, 109, 79, 96, 169, 63, 228, 136, 109, 51, 124, 242, 25, 25, 49, 169, 92, 58, 208, 29, 164, 236, 55, 251, 235, 28, 232, 99, 70, 212, 195, 135, 151, 140, 1, 247, 69, 9, 235, 90, 79, 34, 61, 173, 200, 132, 187, 65, 155, 2, 101, 215, 125, 122, 136, 77, 17, 166, 41, 24, 157, 139, 108, 207, 18, 55, 26, 140, 233, 190, 233, 201, 246, 64, 247, 191, 107, 170, 197, 226, 48, 183, 253, 75, 35, 99, 154, 106, 14, 141, 80, 35, 217, 25, 162, 8, 13, 79, 179, 224, 197, 104, 193, 5, 5, 148, 35, 147, 210, 217, 221, 93, 135, 41, 12, 248, 145, 21, 248, 10, 62, 70, 40, 1, 58, 182, 158, 50, 51, 68, 68, 28, 74, 207, 169, 34, 55, 138, 122, 183, 140, 132, 95, 74, 223, 190, 156, 200, 186, 88, 104, 157, 197, 99, 216, 120, 230, 7, 55, 16, 25, 116, 219, 43, 103, 8, 199, 23, 144, 171, 249, 128, 2, 28, 220, 85, 77, 118, 30, 11, 114, 126, 38, 238, 99, 223, 134, 230, 158, 146, 231, 25, 230, 209, 145, 78, 31, 61, 10, 248, 203, 138, 40, 241, 207, 47, 205, 205, 1, 45, 127, 170, 151, 203, 95, 255, 126, 182, 60, 193, 189, 127, 29, 167, 48, 194, 63, 246, 72, 184, 76, 127, 16, 255, 157, 232, 87, 191 };
	public static final boolean[] RANDOM_BOOL = {false, false, true, true, false, true, true, true, false, true, true, false, true, true, true, false, false, true, true, true, true, false, true, true, true, true, false, true, true, false, false, false, true, true, true, true, true, true, true, false, false, false, true, false, false, true, false, false, true, false, true, true, true, false, false, true, false, false, true, false, false, false, true, false, false, true, false, false, false, true, true, false, false, false, true, false, false, true, true, false, true, false, false, true, true, true, true, false, true, true, true, false, true, false, true, false, false, true, true, false, true, false, true, false, true, false, true, false, false, true, false, false, true, true, false, false, false, false, false, false, true, true, false, false, true, true, false, true, false, false, false, false, false, true, true, true, false, false, false, false, true, false, false, true, false, true, true, false, true, true, true, true, true, true, true, false, false, true, false, false, false, true, false, false, true, false, false, false, true, true, true, true, false, false, true, false, true, false, false, true, false, true, true, true, true, true, false, false, false, true, true, false, true, false, true, false, true, true, true, true, true, false, true, true, false, true, true, true, true, false, false, false, false, false, true, true, true, false, true, false, false, true, false, true, false, false, false, true, false, false, false, false, false, true, false, false, true, false, false, true, true, true, false, true, false, true, true, true, true, false, false, false, false, false, false, true}; 
	private static int RND_INDX = (int)(Math.random()*256);
	
	public static float sin(float theta){
		while(theta < 0) theta += TAU;
		theta %= TAU;
		int index = (int)Math.round(theta*100);
		return SINE[index];
	}
	
	public static float cos(float theta){
		while(theta < 0) theta += TAU;
		theta %= TAU;
		int index = (int)Math.round(theta*100);
		return COSINE[index];
	}
	
	public static float sqrt(float x) {
	    return Float.intBitsToFloat(532483686 + (Float.floatToRawIntBits(x) >> 1));
	}
	
	// PSEUDO RNG
	// Returns random float between 0.0-1.0
	public static float random(){ return RANDOM[(++RND_INDX)%256]; }
	
	// Returns random integer between 0-256
	public static int randomInt(){ return RANDOM_INT[(++RND_INDX)%256]; }

	// Returns random boolean value
	public static boolean randomBoolean(){ return RANDOM_BOOL[(++RND_INDX)%256]; }
	
	// Returns random angle between 0-2pi
	public static float randomAngle(){ return RANDOM[(++RND_INDX)%256]*TAU; }
	
}